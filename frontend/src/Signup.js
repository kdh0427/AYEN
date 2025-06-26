import React from "react";
import { useNavigate } from "react-router-dom";
import "./Signup.css";

function Signup() {
    const navigate = useNavigate();

    const handleSignup = (provider) => {
        console.log(`${provider}로 회원가입 시도`);
        navigate("/scenarios");
    };

    return (
        <div className="login-screen">
            <h1 className="login-title">회원가입이 필요합니다</h1>
            <div className="login-buttons">
                <button className="login-btn kakao" onClick={() => handleSignup("kakao")}>카카오로 회원가입</button>
                <button className="login-btn naver" onClick={() => handleSignup("naver")}>네이버로 회원가입</button>
                <button className="login-btn google" onClick={() => handleSignup("google")}>구글로 회원가입</button>
            </div>
        </div>
    );
}

export default Signup;
