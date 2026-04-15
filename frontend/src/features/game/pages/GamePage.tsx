import { useEffect, useRef, useState } from 'react'
import { KeyBoard } from '../ui/keyboard/KeyBoard'
import { Row } from '../ui/row/Row'
import type { LetterStatus } from '../model/LetterStatus';
import toast from 'react-hot-toast';
import type { GameStatus } from '../model/GameStatus';
import { useNavigate } from 'react-router-dom';
import type { Level } from '../model/Level';
import { Button, Dropdown, type MenuProps } from 'antd';
import axios from 'axios';
import type { GameType } from '../model/GameType';
import { checkWord } from '../api/checkWord';
import { getAttempts } from '../api/getAttempts';
import type { Attempt } from '../model/Attempt';
import { getWord } from '../api/getWord';

export function GamePage(){

    const [gameType, setGameType] = useState<GameType>("daily");
    const [currRow, setCurrRow] = useState(0);
    const [attempts, setAttempts] = useState<string[]>(["", "", "", "", "", ""]);
    const [currAttempt, setCurrAttempt] = useState("");
    const [gameState, setGameState] = useState<GameStatus>("playing");


    const [statuses, setStatuses] = useState<LetterStatus[][]>(
        Array(6).fill(["UNUSED","UNUSED","UNUSED","UNUSED","UNUSED"])
    );

    const KEYS: Array<string[]> = [
        ["Q","W","E","R","T","Y","U","I","O","P"],
        ["A","S","D","F","G","H","J","K","L"],
        ["ENTER","Z","X","C","V","B","N","M","⌫"]
    ];
    const defaultKeysStatues = new Map<string, LetterStatus>();
    
    KEYS.flat().forEach((key) => {
        defaultKeysStatues.set(key, "UNUSED");
    });

    const [keysStatuses, setKeysStatuses] = useState<Map<string, LetterStatus>>(defaultKeysStatues);
    
    const containerRef = useRef<HTMLDivElement>(null);

    const [level, setLevel] = useState<Level>("B1");
    const items: MenuProps['items'] = ["A1", "A2", "B1", "B2"].map((level) => {
        return ({
            key: level,
            label: level,
            onClick: () => setLevel(level as Level)
        });
    });

    useEffect(() => {
        setGameState("playing");
        newGame();
    }, [level]);

    useEffect(() => {
        containerRef.current?.focus();
    }, []);


    useEffect(() => {
        
        let type : GameType = window.location.pathname.includes("personal") ? "personal" : "daily";
        setGameType(type);
        const loadAttempts = async () => {
            if (type == "personal") {
                try {

                    console.log("nem");
                    const userAttempts: Attempt[] = await getAttempts(level, localStorage.getItem("token") || "");

                    const newAttempts = userAttempts.map(a => a.word);
                    const newStatuses = userAttempts.map(a => a.statuses);

                    const rows = Math.min(newAttempts.length, 6);
                    setCurrRow(rows);

                    for (let i = 0; i < 6 - rows; i++) {
                        newAttempts.push("");
                        newStatuses.push(["UNUSED","UNUSED","UNUSED","UNUSED","UNUSED"]);
                    }

                    setAttempts(newAttempts);
                    setStatuses(newStatuses);

                } catch (error) {

                    if (axios.isAxiosError(error)) {
                        if (error.response?.status === 401) {
                            toast.error("Session expired. Please log in again.");
                            localStorage.removeItem("token");
                            navigate("/login");
                            return;
                        }
                    }

                    toast.error("An error occurred. Please try again.");
                }
            }
        };

        loadAttempts();
    }, []);

    const navigate = useNavigate();

    
    async function handleClick(letter: string){

        if (gameState != "playing"){
            return;
        }
        switch(letter) {

            case "⌫":
                setCurrAttempt((prev) => prev.slice(0, -1));
                break;

            case "ENTER":

                if (currAttempt.length !== 5) {
                    toast.error("must be 5 letters");
                    break;
                }

                const attemptIndex = currRow; 

                setAttempts((prev) => {
                    const newAttempts = [...prev];
                    newAttempts[attemptIndex] = currAttempt;
                    return newAttempts;
                });

                let result : LetterStatus[] = [];
                try{
                    result = await checkWord(currAttempt, gameType, level);
                    console.log(currRow);
                } catch (error){


                    if (axios.isAxiosError(error)){
                        console.log(error.code);
                        
                        if (error.response?.status === 401) {
                            toast.error("Session expired. Please log in again.");
                            localStorage.removeItem("token");
                            navigate("/login");
                        }

                        if (error.response?.status === 400) {
                            toast.error("Word not in dictionary");
                        } else {
                            toast.error("An error occurred. Please try again.");
                        }
                    }
                    setCurrAttempt("");
                    return;
                }
                
                console.log(result);
                setStatuses((prev) => {
                    const newStatuses = [...prev];
                    newStatuses[attemptIndex] = result;
                    return newStatuses;
                });


                setKeysStatuses((prev) => {
                    const newKeysStatuses = new Map(prev);

                    result.forEach((stat,i) => {
                        const letter = currAttempt.charAt(i);
                        const oldStat = newKeysStatuses.get(letter) ?? "UNUSED";

                        if (oldStat === "UNUSED" || 
                           (oldStat === "INCLUDES" && stat === "CORRECT")) {
                            newKeysStatuses.set(letter, stat);
                        }
                    });

                    return newKeysStatuses;
                });

                if (result.every((status) => status === "CORRECT")) {
                    toast.success("You win!");
                    setGameState("won");
                    return;
                } else if (currRow === 5) {
                    const word = await getWord(gameType, level);
                    toast.error("You lost! The word was: " + "" + word);
                    setGameState("lost");
                    return;
                }

                setCurrRow((prev) => prev + 1);
                setCurrAttempt("");
                break;

            default:
                setCurrAttempt((prev) => prev.length < 5 ? prev + letter : prev);
        }
    }

    function newGame(){
        setCurrRow(0);
        setAttempts(["", "", "", "", "", ""]);
        setStatuses(Array(6).fill(["UNUSED","UNUSED","UNUSED","UNUSED","UNUSED"]));
        setKeysStatuses(defaultKeysStatues);
        setGameState("playing");
        setCurrAttempt("");
    }

    return (
        <div
            ref={containerRef}
            tabIndex={0}
            className="game-container"
            onKeyDown={(e) => {
                const key = e.key.toUpperCase();

                if (key === "BACKSPACE") handleClick("⌫");
                else if (key === "ENTER") handleClick("ENTER");
                else if (/^[A-Z]$/.test(key)) handleClick(key);
            }}

            style={{display: "flex", flexDirection: "column", alignItems: "center", gap: "20px"}}
        >

            <Dropdown menu={{items}} placement='top' arrow = {true}>
                <Button>{level}</Button>
            </Dropdown>
            {Array.from({length: 6}).map((_, i) => (
                <Row
                    key={i}
                    value={i === currRow ? currAttempt : attempts[i]}
                    isSelected={i === currRow}
                    status={statuses[i]}  
                />
            ))}

            <KeyBoard
                handleClick={handleClick}
                KEYS={KEYS}
                keysStatuses={keysStatuses}
            />

            {(gameState === "won" || gameState === "lost") && (
                <button onClick={() => navigate("/")}>Go to Start</button>
            )}
        </div>
    )
}