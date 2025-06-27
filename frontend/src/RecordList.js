import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./RecordList.css";
import SideMenu from "./SideMenu";

/*
const achievements = [
    { id: 1, title: "노 맨즈 랜드", achievedAt: "2025-06-18 14:20" },
    { id: 2, title: "블랙 아웃", achievedAt: "2025-06-17 21:05" },
    { id: 3, title: "신인 작가 단편선", achievedAt: "2025-06-16 19:30" },
    { id: 4, title: "미. 연. 시", achievedAt: "2025-06-15 10:15" },
    { id: 5, title: "잠실 갈리파", achievedAt: "2025-06-14 09:42" },
    { id: 6, title: "분노의 도로", achievedAt: "2025-06-12 22:10" },
];
*/

const endings = [
    { id: 1, title: "기억의 숲 - 해피엔딩", achievedAt: "2025-06-10 18:30" },
    { id: 2, title: "기억의 숲 - 배드엔딩", achievedAt: "2025-06-09 15:45" },
    { id: 3, title: "도심 속 늑대 - 생존", achievedAt: "2025-06-08 20:10" },
    { id: 4, title: "도심 속 늑대 - 희생", achievedAt: "2025-06-07 13:55" },
];

function RecordList() {
    const [activeTab, setActiveTab] = useState("업적");
    const [achievements, setAchievements] = useState([]);
    const [endings, setEngings] = useState([]);
    const navigate = useNavigate();

    const listToShow = activeTab === "업적" ? achievements : endings;

    useEffect(() => {
        fetch("http://localhost:8080/achievements/me", {
          method: "GET",
          credentials: "include",
        })
          .then((res) => {
            if (!res.ok) throw new Error("업적 조회 실패");
            return res.json();
          })
          .then((data) => {
            setAchievements(data || []);
          })
          .catch((err) => console.error(err));
      }, []);

      useEffect(() => {
        fetch("http://localhost:8080/endings", {
          method: "GET",
          credentials: "include",
        })
          .then((res) => {
            if (!res.ok) throw new Error("결말 조회 실패");
            return res.json();
          })
          .then((data) => {
            setEngings(data || []);
          })
          .catch((err) => console.error(err));
      }, []);

    const handleClick = (item) => {
        const type = activeTab === "업적" ? "업적" : "결말";
        navigate("/recordDetail", {
            state: {
                ...item,
                image_url: "https://cdn.ayen.app/images/achievement10.jpg", // 예시 이미지
                description: type === "업적"
                    ? "이 업적은 특별한 도전을 통해 달성되었습니다."
                    : "이 결말은 시나리오의 특정 선택으로 도달한 엔딩입니다.",
                exp: 100, // 예시 경험치
            },
        });
    };

    return (
        <div className="achievement-page">
            <SideMenu />

            <div className="tabs">
                <div
                    className={`tab ${activeTab === "결말" ? "active" : ""}`}
                    onClick={() => setActiveTab("결말")}
                >
                    결말
                </div>
                <div
                    className={`tab ${activeTab === "업적" ? "active" : ""}`}
                    onClick={() => setActiveTab("업적")}
                >
                    업적
                </div>
            </div>

            <div className="achievement-grid">
                {listToShow.map((a) => (
                    <div
                        key={a.id}
                        className="achievement-card"
                        onClick={() => handleClick(a)}
                        style={{ cursor: "pointer" }}
                    >
                        <div className="image-placeholder" />
                        <div className="achievement-title">{a.title}</div>
                        <div className="achievement-time">{a.achievedAt}</div>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default RecordList;
