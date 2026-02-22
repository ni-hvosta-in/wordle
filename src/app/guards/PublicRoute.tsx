import { Navigate } from "react-router-dom";
import type { GuardProps } from "./GuardProps";

export function PublicRoute({children}: GuardProps){
    if (localStorage.getItem("token")){
        return <Navigate to = "/game"/>
    }
    return children;
}