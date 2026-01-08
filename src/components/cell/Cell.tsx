import "./Cell.css";
interface CellProps {
    value: string;
    className: string;
}

export function Cell({value, className}: CellProps) {
    return (
        <div id="cell" className={className}>
            {value}
        </div>
    )
}