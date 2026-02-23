import { useEffect, useRef, useState } from 'react'
import { KeyBoard } from '../../components/keyboard/KeyBoard'
import { Row } from '../../components/row/Row'
import type { LetterStatus } from '../../types/LetterStatus';
import toast from 'react-hot-toast';
import type { GameStatus } from '../..//types/GameStatus';
import { api } from '../../app/Api';
import { useNavigate } from 'react-router-dom';
import type { Level } from './Level';
import { Button, Dropdown, type MenuProps } from 'antd';

export function GamePage(){

    const [currRow, setCurrRow] = useState(0);
    const [attemps, setAttemps] = useState<string[]>(["", "", "", "", "", ""]);
    const [currAttempt, setCurrAttempt] = useState("");
    const [statuses, setStatuses] = useState<LetterStatus[][]>(
        Array(6).fill(["unused","unused","unused","unused","unused"])
    );

    const containerRef = useRef<HTMLDivElement>(null);
    const [gameState, setGameState] = useState<GameStatus>("playing");
    const [level, setLevel] = useState<Level>("B1");

    const items: MenuProps['items'] = ["A1", "A2", "B1", "B2"].map((level) => {
        return ({
            key: level,
            label: level,
            onClick: () => setLevel(level as Level)
        });
    });

    useEffect(() => {
        containerRef.current?.focus();
    }, []);

    const navigate = useNavigate();
    const KEYS: Array<string[]> = [
        ["Q","W","E","R","T","Y","U","I","O","P"],
        ["A","S","D","F","G","H","J","K","L"],
        ["ENTER","Z","X","C","V","B","N","M","⌫"]
    ];

    const defaultKeysStatues = new Map<string, LetterStatus>();
    KEYS.flat().forEach((key) => {
        defaultKeysStatues.set(key, "unused");
    });

    const [keysStatuses, setKeysStatuses] = useState<Map<string, LetterStatus>>(defaultKeysStatues);

    async function checkWord(value: string): Promise<LetterStatus[]> {
        const response = await api.post("/game/daily/check", {
            attempt: value,
            level: level
        });
        return response.data.statuses;
    }

    async function handleClick(letter: string){

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

                setAttemps((prev) => {
                    const newAttemps = [...prev];
                    newAttemps[attemptIndex] = currAttempt;
                    return newAttemps;
                });

                const result = await checkWord(currAttempt);

                setStatuses((prev) => {
                    const newStatuses = [...prev];
                    newStatuses[attemptIndex] = result;
                    return newStatuses;
                });
                


                setKeysStatuses((prev) => {
                    const newKeysStatuses = new Map(prev);

                    result.forEach((stat,i) => {
                        const letter = currAttempt.charAt(i);
                        const oldStat = newKeysStatuses.get(letter) ?? "unused";

                        if (oldStat === "unused" || 
                           (oldStat === "includes" && stat === "correct")) {
                            newKeysStatuses.set(letter, stat);
                        }
                    });

                    return newKeysStatuses;
                });

                if (result.every((status) => status === "correct")) {
                    toast.success("You win!");
                    setGameState("won");
                    return;
                } else if (currRow === 5) {
                    const response = await api.get("/game/daily/word", {params: {level: level}});
                    toast.error("You lost! The word was: " + "" + response.data);
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
        setAttemps(["", "", "", "", "", ""]);
        setStatuses(Array(6).fill(["unused","unused","unused","unused","unused"]));
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
                    value={i === currRow ? currAttempt : attemps[i]}
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