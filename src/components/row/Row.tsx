import type { LetterStatus } from "../../types/LetterStatus";
import { Cell } from "../cell/Cell";
import "./Row.css";
interface RowProps {
    value: string;
    isSelected: boolean;
    status: LetterStatus[];
}

export function Row({value, isSelected, status}: RowProps){

    return (
        <div className="row-wrapper">
            <div id="row" className={isSelected ? "selected" : ""} >
                {Array.from({length: 5}).map((_, i) => (
                    <Cell key={i} value={value[i]} className={status[i]}/>
                ))}
            </div>
        </div>
    )
}