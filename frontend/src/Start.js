import React from "react";
import "./Start.css";

function Start({ onStart }) {
    return (
        <div className="start-screen">
            <div className="start-title">📖 AYEN</div>
            <div className="start-subtitle">당신의 선택으로 완성되는 스토리</div>
            <button className="start-button" onClick={onStart}>시작하기</button>
        </div>
    );
}

export default Start;
