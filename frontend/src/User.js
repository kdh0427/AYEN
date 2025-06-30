import React, { useEffect, useState } from "react";
import "./User.css";
import SideMenu from "./SideMenu";

const maxExp = 1500;

function User({ onLogout }) {
    const [user, setUser] = useState(null);
    const [errorMsg, setErrorMsg] = useState("");
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        setLoading(true);
        fetch("/api/users/my")
            .then((res) => res.json())
            .then((response) => {
                if (response.code === 200) {
                    setUser(response.data);
                    setErrorMsg("");
                } else if (response.code === 404) {
                    setErrorMsg("사용자 정보를 찾을 수 없습니다.");
                } else {
                    setErrorMsg("사용자 정보를 불러오는 데 실패했습니다.");
                }
            })
            .catch(() => {
                setErrorMsg("서버와의 통신에 실패했습니다.");
            })
            .finally(() => setLoading(false));
    }, []);

    if (loading) {
        return (
            <div className="user-page-container">
                <SideMenu />
                <p className="loading-text">로딩 중...</p>
            </div>
        );
    }

    if (errorMsg) {
        return (
            <div className="user-page-container">
                <SideMenu />
                <p className="error-text">{errorMsg}</p>
            </div>
        );
    }

    if (!user) {
        return null;
    }

    const expPercent = Math.min((user.exp / maxExp) * 100, 100);

    return (
        <div className="user-page-container">
            <SideMenu />
            <h2 className="profile-title">사용자 정보</h2>

            <div className="profile-header">
                <div className="avatar"></div>
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
                <div><strong>시나리오 진행:</strong> {user.ending_count}개</div>
            </div>

            <button className="logout-button" onClick={onLogout}>로그아웃</button>
        </div>
    );
}

export default User;
