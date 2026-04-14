import { api } from "../../../app/Api";
import type { GameType } from "../model/GameType";
import type { Level } from "../model/Level";

export async function getWord(gameType : GameType, level : Level) : Promise<string>{

    let response;
    if (gameType == "daily"){
        response = await api.get(`/game/${gameType}/word`, {params: {level: level}});
    } else {
        response = await api.get(`/game/${gameType}/word`, {params: {level: level},
            headers: {
                Authorization: `Bearer ${localStorage.getItem("token")}`
            }}); 
    }

    return response.data;
}