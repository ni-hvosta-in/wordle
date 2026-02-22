import { createBrowserRouter, RouterProvider } from "react-router-dom"
import "./App.css"
import { Toaster } from 'react-hot-toast';
import { StartPage } from "../pages/startPage/StartPage"
import { GamePage } from "../pages/gamePage/GamePage"
import { AuthPage } from "../pages/authPage/AuthPage";
import { PublicRoute } from "./guards/PublicRoute";
import { ProtectedRoute } from "./guards/ProtectedRoute";

function App() {

    const router = createBrowserRouter([
        {
            path: "/",
            element: <StartPage/>
        }, 
        {
            path: "/game", 
            element: (
                <ProtectedRoute>
                    <GamePage/>
                </ProtectedRoute>
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