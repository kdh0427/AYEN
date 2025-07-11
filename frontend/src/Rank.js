import React, { useEffect, useState } from "react";
import "./Rank.css";
import SideMenu from "./SideMenu";

function Rank() {
  const [mockRanking, setRanking] = useState([]);
  const [currentUserId, setCurrentUserId] = useState(null); // 로그인 유저 ID 상태 추가

  useEffect(() => {
    fetch("http://localhost:8080/users/rankings", {
      method: "GET",
      credentials: "include",
    })
      .then((res) => res.json())
      .then((json) => {
        if (json.rankings && Array.isArray(json.rankings)) {
          setRanking(json.rankings);
          setCurrentUserId(json.currentUserId); // 상태에 저장
        } else {
          console.error("랭킹 데이터가 올바른 배열이 아닙니다:", json);
          setRanking([]);
        }
      })
      .catch((err) => console.error("❌ 요청 실패:", err));
  }, []);

  return (
    <div className="rank-page">
      <SideMenu />
      <h2 className="rank-title">🏆 랭킹</h2>
      <p className="rank-subtitle">상위 유저들과 나의 위치를 확인해 보세요!</p>
      <ul className="rank-list">
        {mockRanking.map((user, index) => (
          <li
            key={user.id}
            className={`rank-item ${user.id === currentUserId ? "me" : ""}`} // 여기서 상태 사용
          >
            <span className="rank-position">#{index + 1}</span>
            <span className="rank-name">
              {user.name} ({user.social_type})
            </span>
            <span className="rank-level">LV. {user.level}</span>
            <span className="rank-achieve">🏅 {user.achievementCount}개</span>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default Rank;
