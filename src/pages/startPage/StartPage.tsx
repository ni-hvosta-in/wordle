import styled from "styled-components"
import "./startPage.css"
import { Button } from "antd";
import { useNavigate } from "react-router-dom";

const Wrapper = styled.div`
    display: flex;
    flex-direction: row;
    gap: 20px;
`;
export function StartPage(){

    const navigate = useNavigate();
    const token = localStorage.getItem("token");
    console.log(token);
    return (
        <Wrapper>
            <Button onClick={() => navigate("/game/daily")}>Daily game</Button>
            <Button onClick={() => token ? navigate("/game/personal") : navigate("/login")}>
                {token? "Personal game" : "Login"}
            </Button>
        </Wrapper>
    )
}