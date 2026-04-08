import { createBrowserRouter, RouterProvider } from "react-router-dom"
import "./App.css"
import { Toaster } from 'react-hot-toast';
import { StartPage } from "../features/start/pages/StartPage"
import { GamePage } from "../features/game/pages/GamePage"
import { AuthPage } from "../features/auth/pages/AuthPage";
import { PublicRoute } from "./guards/PublicRoute";
import { ProtectedRoute } from "./guards/ProtectedRoute";

function App() {

    const router = createBrowserRouter([
        {
            path: "/",
            element: <StartPage/>
        }, 
        {
            path: "/game/personal", 
            element: (
                <ProtectedRoute>
                    <GamePage/>
                </ProtectedRoute>
            )
        }, 
        {
            path : "/game/daily",
            element: (
                <PublicRoute>
                    <GamePage/>
                </PublicRoute>
            )
        },
        {
            path: "/login",
            element: (
                <PublicRoute>
                    <AuthPage/>
                </PublicRoute>
            )
        }
    ])
    return (
        <>
            <RouterProvider router={router}/>
            <Toaster position='top-center'/>
        </>
    )
}

export default App