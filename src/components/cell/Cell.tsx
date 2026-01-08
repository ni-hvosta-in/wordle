import "./Cell.css";
interface CellProps {
    value: string;
    targetValue: string;
    isCheck: boolean;
}

export function Cell({value, targetValue, isCheck}: CellProps) {
    return (
        <div id="cell" className={value === targetValue && isCheck === true ? "correct" : ""}>
            {value}
        </div>
    )
}