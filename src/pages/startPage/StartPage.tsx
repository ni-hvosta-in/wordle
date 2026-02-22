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
    return (
        <Wrapper>
            <Button onClick={() => navigate("/game")}>Daily game</Button>
            <Button onClick={() => navigate("/login")}>Login</Button>
        </Wrapper>
    )
}