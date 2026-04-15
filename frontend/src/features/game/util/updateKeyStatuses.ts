import type { LetterStatus } from "../model/LetterStatus";

export function updateKeyStatuses(status: LetterStatus[], attempt: string, prev: Map<string, LetterStatus>) : Map<string, LetterStatus>{

    const newKeysStatuses = new Map(prev);

    status.forEach((stat,i) => {
        const letter = attempt.charAt(i);
        const oldStat = newKeysStatuses.get(letter) ?? "UNUSED";

        if (oldStat === "UNUSED" || 
        (oldStat === "INCLUDES" && stat === "CORRECT")) {
            newKeysStatuses.set(letter, stat);
        }
    });

    return newKeysStatuses;

}