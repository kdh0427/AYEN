import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./RecordList.css";
import SideMenu from "./SideMenu";

function RecordList() {
    const [activeTab, setActiveTab] = useState("업적");
    const [achievements, setAchievements] = useState([]);
    const [endings, setEndings] = useState([]);
    const [loading, setLoading] = useState(true);
    const [errorMsg, setErrorMsg] = useState("");

    const navigate = useNavigate();

    useEffect(() => {
        setLoading(true);
        const endpoint = activeTab === "업적" ? `${process.env.REACT_APP_API_URL}/achievements/me` : `${process.env.REACT_APP_API_URL}/endings/me`;

        fetch(endpoint, { credentials: "include" })
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

    const handleClick = async (item) => {
        try {
            const endpoint = activeTab === "업적"
                ? `${process.env.REACT_APP_API_URL}/achievements/detail/${item.id}`
                : `${process.env.REACT_APP_API_URL}/endings/detail/${item.id}`;

            const res = await fetch(endpoint, {
                method: "GET",
                credentials: "include",  // 👈 인증 쿠키 포함 중요
            });
            const response = await res.json();

            if (response.code === 200) {
                navigate("/recordDetail", {
                    state: {
                        ...response.data,
                        type: activeTab
                    }
                });
            } else {
                alert("상세 정보를 불러오지 못했습니다.");
            }
        } catch (error) {
            console.error("❌ 요청 실패:", error);
            alert("상세 정보를 불러오는 중 오류가 발생했습니다.");
        }
    };

    return (
        <div className="achievement-page">
            <div className="top-bar">
                <SideMenu />
                <div className="logo">
                    📖 AYEN
                </div>
            </div>

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
                            {a.url ? (
                                <div className="image-placeholder">
                                    <img src={a.url} alt={a.title} />
                                </div>
                            ) : (
                                <div className="image-placeholder" />
                            )}
                            <div className="achievement-title">{a.title}</div>
                            <div className="achievement-time">{new Date(a.achieved_at).toLocaleDateString()}</div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

export default RecordList;