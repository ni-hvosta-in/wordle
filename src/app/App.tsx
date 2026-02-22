import { createBrowserRouter, RouterProvider } from "react-router-dom"
import "./App.css"
import { Toaster } from 'react-hot-toast';
import { StartPage } from "../pages/startPage/StartPage"
import { GamePage } from "../pages/gamePage/GamePage"
import { AuthPage } from "../pages/authPage/AuthPage";

function App() {

    const router = createBrowserRouter([
        {
            path: "/",
            element: <StartPage/>
        }, 
        {
            path: "/game", 
            element: <GamePage/>
        }, 
        {
            path: "/login",
            element: <AuthPage/>
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