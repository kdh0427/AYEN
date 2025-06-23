import React from "react";
import "./Rank.css";
import SideMenu from "./SideMenu";

const mockRanking = [
    { id: 1, name: "용감한 고블린", level: 17, achievements: 9 },
    { id: 2, name: "검은 늑대", level: 15, achievements: 11 },
    { id: 3, name: "빛의 정령", level: 14, achievements: 7 },
    { id: 4, name: "모험가", level: 12, achievements: 6 }, // 현재 유저
    { id: 5, name: "푸른 달", level: 11, achievements: 4 },
];

function Rank() {
    return (
        <div className="rank-page">
            <SideMenu />
            <h2 className="rank-title">🏆 랭킹</h2>
            <p className="rank-subtitle">상위 유저들과 나의 위치를 확인해 보세요!</p>
            <ul className="rank-list">
                {mockRanking.map((user, index) => (
                    <li key={user.id} className={`rank-item ${user.name === "모험가" ? "me" : ""}`}>
                        <span className="rank-position">#{index + 1}</span>
                        <span className="rank-name">{user.name}</span>
                        <span className="rank-level">LV. {user.level}</span>
                        <span className="rank-achieve">🏅 {user.achievements}개</span>
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default Rank;
