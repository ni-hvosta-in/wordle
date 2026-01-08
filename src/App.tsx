import { useEffect, useState } from 'react'
import './App.css'
import { KeyBoard } from './components/keyboard/KeyBoard'
import { Row } from './components/row/Row'

function App() {

    const misteryWord = "REACT";
    const [currRow, setCurrRow] = useState(0);
    const [attemps, setAttemps] = useState<string[]>(["", "", "", "", ""]);
    const [checks, setChecks] = useState<boolean[]>([false, false, false, false, false]);
    const KEYS: Array<string[]> = [
        ["Q","W","E","R","T","Y","U","I","O","P"],
        ["A","S","D","F","G","H","J","K","L"],
        ["ENTER","Z","X","C","V","B","N","M","⌫"]
    ];

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

    return (
        <>  
            {Array.from({length: 5}).map((_, i) => (
                <Row key={i} value={attemps[i]} isSelected={i == currRow} targetWord={misteryWord} isCheck = {checks[i]}/>
            ))} 
            <KeyBoard  incrementRow={setCurrRow} setAttemps={setAttemps} currRow={currRow} KEYS={KEYS}/>
        </>
    )
}

export default App