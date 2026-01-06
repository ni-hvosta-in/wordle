import "./Cell.css";
interface CellProps {
    value: string;
}

export function Cell({value}: CellProps) {
    return (
        <div className="cell">
            {value}
        </div>
    )
}