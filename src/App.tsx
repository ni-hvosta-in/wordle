import { useEffect, useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import { KeyBoard } from './keyboard/KeyBoard'

function App() {

    const [input, setInput] = useState("")
    useEffect(() => {
        console.log("Current input:", input);
    }, [input])
    return (
        <>
            <div>
                <h1>{input}</h1>
            </div>
            <KeyBoard setInput={setInput} />
        </>
    )
}

export default App
