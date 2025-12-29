interface KeyBoardProps {
    setInput: React.Dispatch<React.SetStateAction<string>>;
}

export function KeyBoard({setInput}: KeyBoardProps) {
    const KEYS: Array<string[]> = [
    ["Q","W","E","R","T","Y","U","I","O","P"],
    ["A","S","D","F","G","H","J","K","L"],
    ["ENTER","Z","X","C","V","B","N","M","⌫"]
    ];

    function handleClick(letter: string){
        switch(letter) {
            case "⌫":
                setInput((prev) => prev.slice(0, -1));
                break;
            default:
                setInput((prev) => prev + letter);
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