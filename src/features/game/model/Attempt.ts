import type { LetterStatus } from "./LetterStatus";

export interface Attempt {
    word: string;
    statuses: LetterStatus[];
}