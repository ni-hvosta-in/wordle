import React, {useState } from "react";
import { useNavigate } from "react-router-dom";
import type { AuthType } from "./AuthType";
import "./AuthForm.css";
import { api } from "../../app/Api";
import { notification } from "antd";
import axios from "axios";
export function AuthForm() {

    const [typeOfAuth, setTypeOfAuth] = useState<AuthType>("");
    const [login, setLogin] = useState("");
    const [password, setPassword] = useState("");
    const [repeatPassword, setRepeatPassword] = useState("");
    const navigate = useNavigate();

    async function postForm(event: React.FormEvent<HTMLFormElement>) : Promise<void>{
        
        event.preventDefault();
        
        if (typeOfAuth === "SignUp" && password !== repeatPassword) {
            notification.error({
                message: "Error",
                description: "Passwords do not match"
            });
            return;
        }
        
        try{

            const endPoint : string = `/auth/${typeOfAuth === "SignIn" ? "login" : "register"}` 
            const response = await api.post(endPoint, {
                login: login,
                password: password
            });

            console.log(response);
            notification.success({
                message: "Success",
            });
            navigate("/game");

            
        } catch (error) {
            console.error("Auth error:", error);

            let errorMessage = "Service unavailable"

            if (axios.isAxiosError(error)) {
                errorMessage =
                    error.response?.data?.message ||
                    error.message ||
                    errorMessage;
            }

            notification.error({
                message: "Auth error",
                description: errorMessage
            });
        }
            
    }

    const renderContent = () => {
        if (typeOfAuth == "") {
            return (
                    <div className="authForm">
                        <button onClick={() => setTypeOfAuth("SignIn")}>{"Sign in"}</button>
                        <button onClick={() => setTypeOfAuth("SignUp")}>{"Sign up"}</button>
                    </div>
                    );
        } else {
            
            return (

                <form className="authForm" onSubmit={postForm}> 
                    <input type="text" placeholder="Введите логин" onChange={(event) => {setLogin(event.target.value)}}/>
                    <input type="password" placeholder="Введите пароль" onChange={(event) => {setPassword(event.target.value)}}/>
                    {
                        typeOfAuth === "SignUp" &&
                        <input 
                            type="password"
                            placeholder="Повторите пароль" 
                            onChange={(event) => {setRepeatPassword(event.target.value)}}/>
                    }

                    <button type = "submit">{typeOfAuth === "SignIn" ? "Sign in" : "Sign up"}</button>
                    <button onClick={() => setTypeOfAuth("")} type="button">Back</button>
                    
                </form>
                
                );

        }
    }

    return (
        <div>
           {renderContent()}
        </div>
    );
}