import {api} from "../../../app/Api";
import type { Level } from "../model/Level";
import type { ProfileInfo } from "../model/ProfileInfo";

export async function getProfileInfo(level: Level): Promise<ProfileInfo> {
    const response = api.get("/game/personal/profile", {
        params: {level},
        headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`
        }
    }); 

    return (await response).data;
}

