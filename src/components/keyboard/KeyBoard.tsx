import type { LetterStatus } from "../../types/LetterStatus";
import "./KeyBoard.css"
interface KeyBoardProps {

    handleClick: (letter: string) => void;
    KEYS: Array<string[]>;
    keysStatuses: Map<string, LetterStatus>;
}

export function KeyBoard({handleClick, KEYS, keysStatuses}: KeyBoardProps) {

    return (
        <div>
            {KEYS.map((row, i) => (
                <div key={i}>
                    {row.map((letter, j) => (
                        <button key={j} onClick={() => handleClick(letter)} className={"keyboard-" + keysStatuses.get(letter)?.toLowerCase()}>{letter}</button>
                    ))}
                </div>
            ))}
        </div>
    );
}