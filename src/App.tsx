import { useEffect, useState } from 'react'
import './App.css'
import { KeyBoard } from './components/keyboard/KeyBoard'
import { Row } from './components/row/Row'

function App() {

    const [currRow, setCurrRow] = useState(0);
    const [attemps, setAttemps] = useState<string[]>(["", "", "", "", ""]);
    useEffect(() => {
        console.log(attemps, currRow);
    }, [attemps])
    return (
        <>  
            {Array.from({length: 5}).map((_, i) => (
                <Row key={i} value={attemps[i]} isSelected={i == currRow}/>
            ))} 
            <KeyBoard  incrementRow={setCurrRow} setAttemps={setAttemps} currRow={currRow} />
        </>
    )
}

export default App