import type { LetterStatus } from "../../model/LetterStatus";
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