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

    const defaultKeysStatues = new Map<string, LetterStatus>();
    KEYS.flat().forEach((key) => {
        defaultKeysStatues.set(key, "unused");
    })

    const statuses = useMemo(() => {
        const defaultStatus: LetterStatus[] = ["unused", "unused", "unused", "unused", "unused"];
        return attemps.map((attemp, i) => (
            checks[i] ? checkWord(attemp) : defaultStatus
        )) 
    }, [attemps, checks]);

    const [keysStatuses, setKeysStatuses] = useState<Map<string, LetterStatus>>(defaultKeysStatues);
    useEffect(() => {
        if (currRow != 0){
            setChecks((prev) => {
                const newChecks = [...prev];
                newChecks[currRow-1] = true;
                return newChecks;
            });
            setKeysStatuses((prev) => {
                const newKeysStatuses = new Map(prev);
                const stats: LetterStatus[] = checkWord(attemps[currRow-1]);
                stats.forEach((stat,i) => {
                    const oldStat = newKeysStatuses.get(attemps[currRow-1].charAt(i))!;
                    switch(oldStat) {
                        case "unused": 
                            newKeysStatuses.set(attemps[currRow-1].charAt(i), stat);
                            console.log();
                            break;
                        case "includes":

                            if (stat == "correct"){
                                newKeysStatuses.set(attemps[currRow-1].charAt(i), stat);
                            }
                            break;
                    }
                });
                return newKeysStatuses;
             })
        }

        if (misteryWord === attemps[currRow-1]){
            alert("Good job, boy");
           newGame();
        }
        if (currRow > 4) {
            alert("Game Over! The word was " + misteryWord);
            newGame();
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
            if (!letterCounts.has(value[i])){
                status[i] = "wrong";
            }
        }

        return status;

    }

    function newGame(){
        setCurrRow(0);
        setAttemps(["", "", "", "", ""]);
        setChecks([false, false, false, false, false]);
        setKeysStatuses(defaultKeysStatues);
    }
    return (
        <>  
            {Array.from({length: 5}).map((_, i) => (
                <Row key={i} value={attemps[i]} isSelected={i == currRow} status={statuses[i]}/>
            ))} 
            <KeyBoard  incrementRow={setCurrRow} setAttemps={setAttemps} currRow={currRow} KEYS={KEYS} keysStatuses={keysStatuses}/>
        </>
    )
}

export default App