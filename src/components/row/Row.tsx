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
    
    const letterCounts = new Map<string, number>();
    targetWord.split("").forEach((letter) => {
        letterCounts.set(letter, (letterCounts.get(letter) ?? 0) + 1);
    });
    const generateCells = () => {
        
        let content = [];
        let className = ""; 
        for (let i = 0; i < 5; i++){
            className = "";
            if (letterCounts.has(value[i]) && isCheck && letterCounts.get(value[i])! > 0){

                
                if (targetWord.charAt(i) == value[i]){
                    className = "correct";
                } else{
                    className = "includes";
                }
                letterCounts.set(value[i], letterCounts.get(value[i])! -1 );
                console.log(letterCounts);
            }
            
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