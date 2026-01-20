import { useEffect, useMemo, useState } from 'react'
import './App.css'
import { KeyBoard } from './components/keyboard/KeyBoard'
import { Row } from './components/row/Row'
import type { LetterStatus } from './types/LetterStatus';

function App() {

    const misteryWord = "APPLE";
    const [currRow, setCurrRow] = useState(0);
    const [attemps, setAttemps] = useState<string[]>(["", "", "", "", ""]);
    const [checks, setChecks] = useState<boolean[]>([false, false, false, false, false]);
    
    const KEYS: Array<string[]> = [
        ["Q","W","E","R","T","Y","U","I","O","P"],
        ["A","S","D","F","G","H","J","K","L"],
        ["ENTER","Z","X","C","V","B","N","M","⌫"]
    ];

    const statuses = useMemo(() => {
        const defaultStatus: LetterStatus[] = ["unused", "unused", "unused", "unused", "unused"];
        return attemps.map((attemp, i) => (
            checks[i] ? checkWord(attemp) : defaultStatus
        )) 
    }, [attemps, checks]);

    useEffect(() => {
        if (currRow != 0){
            setChecks((prev) => {
                const newChecks = [...prev];
                newChecks[currRow-1] = true;
                return newChecks;
            });
        }

        if (currRow > 4) {
            alert("Game Over! The word was " + misteryWord);
            setCurrRow(0);
            setAttemps(["", "", "", "", ""]);
        }
    }, [currRow])

    function checkWord(value: string): LetterStatus [] {

        const status: LetterStatus[] = ["unused", "unused", "unused", "unused", "unused"];

        const letterCounts = new Map<string, number>();
        misteryWord.split("").forEach((letter) => {
            letterCounts.set(letter, (letterCounts.get(letter) ?? 0) + 1);
        });

        for (let i = 0; i < 5; i++){
    
            if (letterCounts.has(value[i]) && letterCounts.get(value[i])! > 0){
                
                if (misteryWord.charAt(i) == value[i]){
                    status[i] = "correct";
                } else{
                    status[i] = "includes";
                }
                letterCounts.set(value[i], letterCounts.get(value[i])! -1 );
            }
        }

        return status;

    }
    return (
        <>  
            {Array.from({length: 5}).map((_, i) => (
                <Row key={i} value={attemps[i]} isSelected={i == currRow} status={statuses[i]}/>
            ))} 
            <KeyBoard  incrementRow={setCurrRow} setAttemps={setAttemps} currRow={currRow} KEYS={KEYS} />
        </>
    )
}

export default App