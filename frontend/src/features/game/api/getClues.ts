import type { Level } from "../model/Level";
import type { Clue } from "../model/Clue";
import { api } from "../../../app/Api";
export async function getClues(level: Level): Promise<Clue[]> {
  const response = await api.get<Clue[]>("game/personal/clues", {
    params: { level },
        headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`
        }
    
  });

  return response.data;
}