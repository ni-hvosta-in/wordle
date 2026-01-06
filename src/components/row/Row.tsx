import { Cell } from "../cell/Cell";
import "./Row.css";
interface RowProps {
    value: string;
    isSelected: boolean;
}

export function Row({value, isSelected}: RowProps){
    
    return (
        <div className="row-wrapper">
            <div id="row" className={isSelected ? "selected" : ""} >
                {Array.from({length: 5}).map((_, i) => (
                    <Cell key={i} value={value[i] || ""} />
                ))}
            </div>
        </div>
    )
}