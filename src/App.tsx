import { useEffect, useMemo, useRef, useState } from 'react'
import './App.css'
import { KeyBoard } from './components/keyboard/KeyBoard'
import { Row } from './components/row/Row'
import type { LetterStatus } from './types/LetterStatus';
import toast, { Toaster } from 'react-hot-toast';
import type { GameStatus } from './types/GameStatus';
function App() {

    const misteryWord = "APPLE";
    const [currRow, setCurrRow] = useState(0);
    const [attemps, setAttemps] = useState<string[]>(["", "", "", "", "", ""]);
    const [currAttempt, setCurrAttempt] = useState("");
    const [checks, setChecks] = useState<boolean[]>([false, false, false, false, false, false]);
    const containerRef = useRef<HTMLDivElement>(null);
    
    const [gameState, setGameState] = useState<GameStatus>("playing");

    useEffect(() => {
        containerRef.current?.focus();
    }, []);
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
            setGameState("won");
        } else {
            if (currRow > 5) {
                setGameState("lost");
            }
        }
    }, [currRow]);

    useEffect(() => {
        if (gameState === "won") {
            toast.success("Good job, boy");
            setTimeout(() => {
                newGame();
            }, 800); // 👈 время показать зелёную строку
        }

        if (gameState === "lost") {
            toast.error("Game Over! The word was " + misteryWord);
            setTimeout(() => {
                newGame();
            }, 800);
        }
    }, [gameState]);


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

    function handleClick(letter: string){
        switch(letter) {
            case "⌫":
                setCurrAttempt((prev) => prev.slice(0, -1));
                break;
        
            case "ENTER":
                

                if (currAttempt.length !== 5) {
                    toast.error("must be 5 letters");
                    break;
                }

                setCurrRow((prev) => prev + 1);
                setAttemps((prev) => {
                    const newAttemps = [...prev];
                    newAttemps[currRow] = currAttempt;
                    return newAttemps;
                });

                setCurrAttempt("");
                break;

            default:
                setCurrAttempt((prev) => prev.length < 5 ? (prev || "") + letter : prev);
        }
        
    }

    function newGame(){
        setCurrRow(0);
        setAttemps(["", "", "", "", "", ""]);
        setChecks([false, false, false, false, false, false]);
        setKeysStatuses(defaultKeysStatues);
        setGameState("playing");
        setCurrAttempt("");
    }

    return (
        <>
            <Toaster position='top-center'/>
            <div ref={containerRef}tabIndex={0} className="game-container"
    onKeyDown={(e) => {
                const key = e.key.toUpperCase();

                if (key === "BACKSPACE") handleClick("⌫");
                else if (key === "ENTER") handleClick("ENTER");
                else if (/^[A-Z]$/.test(key.toUpperCase())) handleClick(key.toUpperCase());
            }}>  
                {Array.from({length: 6}).map((_, i) => (
                    <Row key={i} value={i == currRow ? currAttempt : attemps[i]} isSelected={i == currRow} status={statuses[i]}/>
                ))} 
                <KeyBoard  handleClick={handleClick} KEYS={KEYS} keysStatuses={keysStatuses}/>
            </div>
        </>
    )
}

export default App