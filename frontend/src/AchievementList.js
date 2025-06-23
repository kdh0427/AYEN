import React from "react";
import "./AchievementList.css";
import SideMenu from "./SideMenu";

const achievements = [
    { id: 1, title: "노 맨즈 랜드", achievedAt: "2025-06-18 14:20" },
    { id: 2, title: "블랙 아웃", achievedAt: "2025-06-17 21:05" },
    { id: 3, title: "신인 작가 단편선", achievedAt: "2025-06-16 19:30" },
    { id: 4, title: "미. 연. 시", achievedAt: "2025-06-15 10:15" },
    { id: 5, title: "잠실 갈리파", achievedAt: "2025-06-14 09:42" },
    { id: 6, title: "분노의 도로", achievedAt: "2025-06-12 22:10" },
];

function AchievementList() {
    return (
        <div className="achievement-page">
            <SideMenu />

            <div className="tabs">
                <div className="tab">결말</div>
                <div className="tab active">업적</div>
            </div>

            <div className="achievement-grid">
                {achievements.map((a) => (
                    <div key={a.id} className="achievement-card">
                        <div className="image-placeholder" />
                        <div className="achievement-title">{a.title}</div>
                        <div className="achievement-time">{a.achievedAt}</div>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default AchievementList;
