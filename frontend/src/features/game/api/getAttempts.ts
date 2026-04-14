import { api } from "../../../app/Api";
import type { Attempt } from "../model/Attempt";
import type { Level } from "../model/Level";

export async function getAttempts(level : Level, token : string) : Promise<Attempt[]>{

    let response = await api.get("/game/personal/attempts", 
        {headers: {"Authorization": `Bearer ${token}`},
        params: {level : level}});

    return response.data.attempts;
}