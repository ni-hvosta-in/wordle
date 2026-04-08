import type { LetterStatus } from "../../types/LetterStatus";
import "./Cell.css";
interface CellProps {
    value: string;
    className: LetterStatus;
}

export function Cell({value, className}: CellProps) {
    return (
        <div id="cell" className={className.toLowerCase()}>
            {value}
        </div>
    )
}