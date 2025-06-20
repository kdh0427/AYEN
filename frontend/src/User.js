import React from "react";
import "./User.css";

const user = {
    id: 1,
    email: "ayen_user@gmail.com",
    user_token: "ayen123456abcdef",
    name: "AYEN용사",
    social_type: "google",
    level: 5,
    exp: 1280,
    achievement_count: 7,
    scenario_play_count: 4,
    created_at: "2025-06-01T10:30:00",
};

const maxExp = 1500;
const expPercent = Math.min((user.exp / maxExp) * 100, 100);

function User({ onLogout, onMenuClick }) {
    return (
        <div className="user-profile">
            <div className="menu-button" onClick={onMenuClick}>☰</div>
            <h2 className="profile-title">사용자 정보</h2>
            <div className="profile-card">
                <div className="profile-header">
                    <div className="avatar">👤</div>
                    <div className="profile-name">{user.name}</div>
                    <div className="profile-social">({user.social_type})</div>
                </div>
                <div className="profile-level">
                    <span className="level-label">Lv. {user.level}</span>
                    <div className="exp-bar">
                        <div className="exp-fill" style={{ width: `${expPercent}%` }} />
                    </div>
                    <div className="exp-text">{user.exp} / {maxExp} XP</div>
                </div>
                <div className="profile-info">
                    <div><strong>이메일:</strong> {user.email}</div>
                    <div><strong>가입일:</strong> {new Date(user.created_at).toLocaleDateString()}</div>
                    <div><strong>획득 업적:</strong> {user.achievement_count}개</div>
                    <div><strong>시나리오 진행:</strong> {user.scenario_play_count}개</div>
                </div>
                <button className="logout-button" onClick={onLogout}>로그아웃</button>
            </div>
        </div>
    );
}

export default User;