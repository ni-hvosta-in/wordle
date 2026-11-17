import './Clue.css';
import { Cell } from '../cell/Cell';

interface ClueProps{
    value : string;
}
export function Clue({value} : ClueProps) {
  return (
    <div className="clue-wrapper">
      <span className="clue-title">Подсказка</span>
      <div className="clue-row">
              {Array.from({length: 5}).map((_, i) => (
                  <Cell key={i} value={value[i]} className={value[i] == " " ? "UNUSED" : "CORRECT"}/>
              ))}
          </div>
    </div>
  );
}
