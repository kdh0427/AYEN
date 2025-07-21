import React, { useEffect, useState } from "react";
import "./User.css";
import SideMenu from "./SideMenu";

function User() {
  const [user, setUser] = useState(null);
  const maxExp = 1500;

  useEffect(() => {
    fetch(`${process.env.REACT_APP_API_URL}/users/my`, {
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
    if (!user) return;

    // 공통 서버 로그아웃 요청
    const serverLogout = () => {
      return fetch(`${process.env.REACT_APP_API_URL}/logout`, {
        method: "POST",
        credentials: "include",
      })
    };

    if (user.social_type === "kakao") {
      serverLogout().then(response => {
        if (response.ok) {
          const clientId = "12ac0203c0f8bfb6661fcf181d95eefd"; // 카카오 REST API 키
          const redirectUri = encodeURIComponent(`${process.env.REACT_APP_API_URL2}`); // 로그아웃 후 리디렉트 URL
          window.location.href = `https://kauth.kakao.com/oauth/logout?client_id=${clientId}&logout_redirect_uri=${redirectUri}`;
        } else {
          alert("로그아웃 실패");
        }
      }).catch(() => alert("네트워크 오류"));

    } else if (user.social_type === "google") {
      // 구글 로그아웃 처리
      if (window.gapi) {
        const auth2 = window.gapi.auth2.getAuthInstance();
        if (auth2) {
          auth2.signOut().then(() => {
            serverLogout().then(response => {
              if (response.ok) {
                window.location.href = "/"; // 구글 로그아웃 후 리다이렉트
              } else {
                alert("로그아웃 실패");
              }
            });
          });
        }
      } else {
        alert("Google API not loaded.");
      }
    }else {
      // 일반 로그아웃 (예: 자체 회원가입 사용자)
      serverLogout().then(response => {
        if (response.ok) {
          window.location.href = "/";
        } else {
          alert("로그아웃 실패");
        }
      }).catch(() => alert("네트워크 오류"));
    }
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
