import React from "react";
//import { useNavigate } from "react-router-dom";
import "./Signup.css";

function Signup() {
    //const navigate = useNavigate();

    return (
        <div className="login-screen">
            <h1 className="login-title">회원가입이 필요합니다</h1>
            <div className="login-buttons">
                <button
                    className="login-btn kakao"
                    onClick={() =>
                        (window.location.href = `${process.env.REACT_APP_API_URL}/oauth2/authorization/kakao`)
                    }
                >
                    카카오로 시작하기
                </button>
                <button
                    className="login-btn google"
                    onClick={() =>
                        (window.location.href = `${process.env.REACT_APP_API_URL}/oauth2/authorization/google`)
                    }
                >
                    구글로 시작하기
                </button>
            </div>
        </div>
    );
}

export default Signup;
