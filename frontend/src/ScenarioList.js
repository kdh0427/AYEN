import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./ScenarioList.css";
import SideMenu from "./SideMenu";

function ScenarioList({ onMenuClick }) {
    const navigate = useNavigate();
    const [scenarios, setScenarios] = useState([]);
    const [loading, setLoading] = useState(true);
    const [errorMsg, setErrorMsg] = useState("");

    useEffect(() => {
        const fetchScenarios = async () => {
            try {
                const res = await fetch("/api/scenarios");
                const data = await res.json();

                if (data.code === 200) {
                    setScenarios(data.data);
                } else if (data.code === 204) {
                    setErrorMsg("시나리오가 존재하지 않습니다.");
                } else {
                    setErrorMsg("시나리오를 불러오는 데 실패했습니다.");
                }
            } catch (error) {
                console.error("Error fetching scenarios:", error);
                setErrorMsg("서버 오류가 발생했습니다.");
            } finally {
                setLoading(false);
            }
        };

        fetchScenarios();
    }, []);

    // 클릭 시 첫 번째 장면으로 이동하도록 수정
    const handleScenarioClick = (scenarioId) => {
        navigate(`/scenarios/${scenarioId}/scenes/1`);
    };

    if (loading) {
        return <div className="scenario-page">로딩 중...</div>;
    }

    if (errorMsg) {
        return <div className="scenario-page">{errorMsg}</div>;
    }

    return (
        <div className="scenario-page">
            <div className="top-bar">
                <SideMenu />
            </div>
            <h2 className="scenario-title">시나리오 선택</h2>
            <div className="scenario-grid">
                {scenarios.map((s) => (
                    <div key={s.id} className="scenario-card" onClick={() => handleScenarioClick(s.id)}>
                        <div className="scenario-image">
                            {s.image_url ? (
                                <img
                                    src={s.image_url}
                                    alt={s.title}
                                    onError={(e) => {
                                        e.target.onerror = null; // 무한 루프 방지
                                        e.target.style.display = "none";
                                        e.target.parentNode.querySelector(".image-placeholder").style.display = "flex";
                                    }}
                                />
                            ) : null}
                            <div className="image-placeholder" style={{ display: s.image_url ? "none" : "flex" }}></div>
                        </div>
                        <div className="scenario-info">
                            <div className="scenario-name">{s.title}</div>
                            <div className="scenario-desc">{s.description}</div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default ScenarioList;
