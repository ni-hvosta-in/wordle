import {api} from "../../../app/Api";
import type { GameType } from "../model/GameType";
import type { LetterStatus } from "../model/LetterStatus";
import type { Level } from "../model/Level";

export async function checkWord(value: string, gameType: GameType, level: Level): Promise<LetterStatus[]> {

        let response;
        if (gameType == "daily"){
            response = await api.post("/game/daily/check", {
                attempt: value,
                level: level
            });
        } else {
            response = await api.post("/game/personal/check", {
                attempt: value,
                level: level
            },
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("token")}`
                }
            });
        }
        return response.data.statuses;
    }
