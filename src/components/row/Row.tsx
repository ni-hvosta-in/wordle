import { useMemo } from "react";
import { Cell } from "../cell/Cell";
import "./Row.css";
interface RowProps {
    value: string;
    isSelected: boolean;
    targetWord: string;
    isCheck: boolean;
}

export function Row({value, isSelected, targetWord, isCheck}: RowProps){
    
    const generateCells = () => {
        
        let content = [];
        let className = ""; 
        for (let i = 0; i < 5; i++){
            className = "";
            if (targetWord[i] == value[i] && isCheck){
                className = "correct";
            } else {
                if (targetWord.includes(value[i]) && isCheck){
                    className = "includes";
                }
            }
            console.log(className, value, isCheck);
            
            content.push(<Cell key={i} value={value[i]} className={className}/>)
        }
        return content;
    };

    return (
        <div className="row-wrapper">
            <div id="row" className={isSelected ? "selected" : ""} >
                {generateCells()}
            </div>
        </div>
    )
}