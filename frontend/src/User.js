import React, { useEffect, useState } from "react";
import "./User.css";
import SideMenu from "./SideMenu";

function User() {
  const [user, setUser] = useState(null);
  const maxExp = 1500;

  useEffect(() => {
    fetch("http://localhost:8080/users/my", {
      method: "GET",
      credentials: "include",
    })
      .then((res) => {
        if (!res.ok) throw new Error("사용자 정보 조회 실패");
        return res.json();
      })
      .then((data) => setUser(data))
      .catch((err) => console.error(err));
  }, []);

  function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(";").shift();
  }

  const handleLogout = () => {
    const csrfToken = getCookie("XSRF-TOKEN");
    fetch("http://localhost:8080/logout", {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
        "X-XSRF-TOKEN": csrfToken,
      },
      credentials: "include",
    })
      .then((response) => {
        if (response.ok) {
          const clientId = "12ac0203c0f8bfb6661fcf181d95eefd"; // 카카오 REST API 키
          const redirectUri = encodeURIComponent("http://localhost:3000"); // 로그아웃 후 리디렉트
          window.location.href = `https://kauth.kakao.com/oauth/logout?client_id=${clientId}&logout_redirect_uri=${redirectUri}`;
        } else {
          alert("로그아웃 실패");
        }
      })
      .catch((err) => {
        console.error(err);
        alert("네트워크 오류");
      });
  };

  // ⚡ 로딩 전엔 로딩 메시지를 띄워서 user가 null일 때 접근되지 않도록 함
  if (!user) {
    return (
      <div className="user-page-container">
        <SideMenu />
        <h2 className="profile-title">사용자 정보</h2>
        <div>로딩중입니다...</div>
      </div>
    );
  }

  // ⚡ user가 로드된 후 아래 로직 실행
  const expPercent = Math.min((user.exp / maxExp) * 100, 100);

  return (
    <div className="user-page-container">
      <div className="top-bar">
        <SideMenu />
        <div className="logo">
          📖 AYEN
        </div>
      </div>
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
        <div className="exp-text">
          {user.exp} / {maxExp} XP
        </div>
      </div>

      <div className="profile-info">
        <div>
          <strong>이메일:</strong> {user.email}
        </div>
        <div>
          <strong>가입일:</strong> {new Date(user.created_at).toLocaleDateString()}
        </div>
        <div>
          <strong>획득 업적:</strong> {user.achievementCount}개
        </div>
        <div>
          <strong>시나리오 진행:</strong> {user.scenario_play_count}개
        </div>
      </div>

      <button className="logout-button" onClick={handleLogout}>
        로그아웃
      </button>
    </div>
  );
}

export default User;
