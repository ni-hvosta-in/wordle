import type React from "react";

interface KeyBoardProps {
    incrementRow: React.Dispatch<React.SetStateAction<number>>;
    setAttemps: React.Dispatch<React.SetStateAction<string[]>>;
    currRow: number;
}

export function KeyBoard({incrementRow, setAttemps, currRow}: KeyBoardProps) {
    const KEYS: Array<string[]> = [
    ["Q","W","E","R","T","Y","U","I","O","P"],
    ["A","S","D","F","G","H","J","K","L"],
    ["ENTER","Z","X","C","V","B","N","M","⌫"]
    ];



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
                        <button key={j} onClick={() => handleClick(letter)}>{letter}</button>
                    ))}
                </div>
            ))}
        </div>
    );
}