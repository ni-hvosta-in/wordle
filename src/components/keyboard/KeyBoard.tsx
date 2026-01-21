import type React from "react";
import { useEffect } from "react";
import type { LetterStatus } from "../../types/LetterStatus";
import "./KeyBoard.css"
interface KeyBoardProps {
    incrementRow: React.Dispatch<React.SetStateAction<number>>;
    setAttemps: React.Dispatch<React.SetStateAction<string[]>>;
    currRow: number;
    KEYS: Array<string[]>;
    keysStatuses: Map<string, LetterStatus>;
}

export function KeyBoard({incrementRow, setAttemps, currRow, KEYS, keysStatuses}: KeyBoardProps) {

    useEffect( () => {
        function handleKeyDown(event: KeyboardEvent) {
            const key = event.key.toUpperCase();
            if (key === "BACKSPACE") {
                handleClick("⌫");
            } else if (key === "ENTER") {
                handleClick("ENTER");
            }
            else if (/^[A-Z]$/.test(key)) {
                handleClick(key);
            }
        }
        window.addEventListener("keydown", handleKeyDown);
        return () => {
            window.removeEventListener("keydown", handleKeyDown);
        };
    }, [currRow]);

    function handleClick(letter: string){
        switch(letter) {
            case "⌫":
                setAttemps((prev) => {
                    const newAttemps = [...prev];
                    newAttemps[currRow] = newAttemps[currRow].slice(0, -1);
                    return newAttemps;
                });

                break;
            
            case "ENTER":
                incrementRow((prev) => prev + 1);
                
                break;

            default:
                setAttemps((prev) => {
                    const newAttemps = [...prev];
                    newAttemps[currRow] = newAttemps[currRow].length < 5 ? (newAttemps[currRow] || "") + letter : newAttemps[currRow];
                    return newAttemps;
                });
        }
        
    }

    return (
        <div>
            {KEYS.map((row, i) => (
                <div key={i}>
                    {row.map((letter, j) => (
                        <button key={j} onClick={() => handleClick(letter)} className={keysStatuses.get(letter)}>{letter}</button>
                    ))}
                </div>
            ))}
        </div>
    );
}