import { Navigate } from "react-router-dom";
import type { GuardProps } from "./GuardProps";

export function ProtectedRoute({children}: GuardProps){
    if (!localStorage.getItem("token")){
        return <Navigate to = "/login"/>
    }
    return children;
}