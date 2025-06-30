import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./RecordList.css";
import SideMenu from "./SideMenu";

function RecordList() {
    const [activeTab, setActiveTab] = useState("업적");
    const [achievements, setAchievements] = useState([]);
    const [endings, setEngings] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        setLoading(true);
        const endpoint = activeTab === "업적" ? "/api/achievements" : "/api/endings";

        fetch(endpoint)
            .then((res) => res.json())
            .then((response) => {
                if (response.code === 200) {
                    if (activeTab === "업적") {
                        setAchievements(response.data);
                    } else {
                        setEndings(response.data);
                    }
                    setErrorMsg("");
                } else if (response.code === 204) {
                    if (activeTab === "업적") {
                        setAchievements([]);
                    } else {
                        setEndings([]);
                    }
                    setErrorMsg("데이터가 존재하지 않습니다.");
                } else {
                    setErrorMsg("데이터를 불러오는 데 실패했습니다.");
                }
            })
            .catch(() => {
                setErrorMsg("서버와의 통신에 실패했습니다.");
            })
            .finally(() => {
                setLoading(false);
            });
    }, [activeTab]);

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

            {loading ? (
                <p className="loading-text">로딩 중...</p>
            ) : errorMsg ? (
                <p className="error-text">{errorMsg}</p>
            ) : (
                <div className="achievement-grid">
                    {listToShow.map((a, idx) => (
                        <div
                            key={idx}
                            className="achievement-card"
                            onClick={() => handleClick(a)}
                            style={{ cursor: "pointer" }}
                        >
                            {a.image_url ? (
                                <img
                                    className="image-placeholder"
                                    src={a.image_url}
                                    alt={a.title}
                                />
                            ) : (
                                <div className="image-placeholder" />
                            )}
                            <div className="achievement-title">{a.title}</div>
                            <div className="achievement-time">{a.achieved_at}</div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

export default RecordList;
