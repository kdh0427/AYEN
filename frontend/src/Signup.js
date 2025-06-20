import React from "react";
import "./Signup.css";

function Signup({ onSignup }) {
    return (
        <div className="login-screen">
            <h1 className="login-title">회원가입이 필요합니다</h1>
            <div className="login-buttons">
                <button className="login-btn kakao" onClick={() => onSignup("kakao")}>카카오로 회원가입</button>
                <button className="login-btn naver" onClick={() => onSignup("naver")}>네이버로 회원가입</button>
                <button className="login-btn google" onClick={() => onSignup("google")}>구글로 회원가입</button>
            </div>
        </div>
    );
}

export default Signup;
