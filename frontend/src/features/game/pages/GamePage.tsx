import { useEffect, useRef, useState } from 'react'
import { KeyBoard } from '../ui/keyboard/KeyBoard'
import { Row } from '../ui/row/Row'
import type { LetterStatus } from '../model/LetterStatus';
import toast from 'react-hot-toast';
import type { GameStatus } from '../model/GameStatus';
import { useNavigate } from 'react-router-dom';
import type { Level } from '../model/Level';
import { type MenuProps } from 'antd';
import axios from 'axios';
import type { GameType } from '../model/GameType';
import { checkWord } from '../api/checkWord';
import { getAttempts } from '../api/getAttempts';
import type { Attempt } from '../model/Attempt';
import { getWord } from '../api/getWord';
import { updateKeyStatuses } from '../util/updateKeyStatuses';
import { Profile } from '../ui/profile/Profile';
import type { ProfileInfo } from '../model/ProfileInfo';
import { getProfileInfo } from '../api/getProfileInfo';
import type { Clue as ClueModel } from '../model/Clue';
import { getClue } from '../api/getClue';
import { Clue } from '../ui/clue/Clue';
import { getClues } from '../api/getClues';
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
            onClick: () => {
                setLevel(level as Level)
                sessionStorage.setItem("level", level);
            }
        });
    });

    const [coins, setCoins] = useState(0);
    const [game_level, setGame_level] = useState(1);
    const [clues, setClues] = useState(" ".repeat(5));

    const navigate = useNavigate();

    const getProfile = async () => {

        const type : GameType = window.location.pathname.includes("personal") ? "personal" : "daily";
        if (type == "daily"){
            return;
        }

        const profileInfo: ProfileInfo = await getProfileInfo(level);
        setCoins(profileInfo.coins);
        setGame_level(profileInfo.gameLevel);

    };

    function newGame(){
        setCurrRow(0);
        setAttempts(["", "", "", "", "", ""]);
        setStatuses(Array(6).fill(["UNUSED","UNUSED","UNUSED","UNUSED","UNUSED"]));
        setKeysStatuses(defaultKeysStatues);
        setGameState("playing");
        setCurrAttempt("");
        setClues(" ".repeat(5));
        containerRef.current?.focus();
    }

    useEffect(() => {
        setGameState("playing");
        newGame();
    }, [level]);

    useEffect(() => {
        containerRef.current?.focus();
    }, []);

    useEffect(() => {
        
        setLevel((sessionStorage.getItem("level") as Level) || "B1");
        const type : GameType = window.location.pathname.includes("personal") ? "personal" : "daily";
        setGameType(type);
        const loadAttempts = async () => {
            if (type == "daily"){
                return;
            }

            try {

                const userAttempts: Attempt[] = await getAttempts(level, localStorage.getItem("token") || "");
                
                if (userAttempts === null){
                    return;
                }

                const newAttempts = userAttempts.map(a => a.word);
                const newStatuses = userAttempts.map(a => a.statuses);

                newStatuses.forEach((status, i) => {
                    setKeysStatuses((prev) => updateKeyStatuses(status, newAttempts[i], prev));
                });
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
        
        };

        const loadClues = async () => {
            if (type == "daily") {
                return;
            }

            const clues2: ClueModel[] = await getClues(level);

            console.log("clues2:", clues2);
            console.log("isArray:", Array.isArray(clues2));

            const newClues = clues.split("");

            clues2.forEach((clue) => {
                newClues[clue.index] = clue.letter;
            });

            setClues(newClues.join(""));
        };
        loadAttempts();
        loadClues();
        getProfile();

    }, [level]);

    async function handleClick(letter: string){

        if (gameState != "playing"){
            return;
        }
        switch(letter) {

            case "⌫":
                setCurrAttempt((prev) => prev.slice(0, -1));
                break;

            case "ENTER": {

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


                setKeysStatuses((prev) => updateKeyStatuses(result, currAttempt, prev));

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
            }

            default:
                setCurrAttempt((prev) => prev.length < 5 ? prev + letter : prev);
        }
    }

    async function help(){
        if (gameState !== "playing") {
            return;
        }

        try {
            const clue: ClueModel = await getClue(level);
            setClues((prev) => {
                const newClues = prev.split("");
                newClues[clue.index] = clue.letter;
                return newClues.join("");
            })

        } catch (error) {
            if (axios.isAxiosError(error) && error.response?.status === 401) {
                toast.error("Session expired. Please log in again.");
                localStorage.removeItem("token");
                navigate("/login");
                return;
            }

            toast.error("An error occurred. Please try again.");
        }
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
        >
            <Profile level={level} game_level={game_level} items={items} coins={coins} isPersonal = {gameType === "personal"} isPlaying = {gameState === "playing"} helpFunction={help}/>
            {gameType === "personal" && <Clue value = {clues}/>}
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

            {gameType === "daily" && (gameState === "won" || gameState === "lost") && (
                <button onClick={() => navigate("/")}>Go to Start</button>
            )}

            {gameType === "personal" && (gameState === "won" || gameState === "lost") &&(
                <button onClick={() => newGame()}>Next level</button>
            )}
        </div>
    )
}