import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./ScenarioList.css";
import SideMenu from "./SideMenu";

function ScenarioList({ onMenuClick }) {
    const[scenarios, setScenario] = useState([]);
    const navigate = useNavigate();
    const [loading, setLoading] = useState(true);
    const [errorMsg, setErrorMsg] = useState("");

    useEffect(() => {

        fetch("http://localhost:8080/scenarios", {
          method: "GET",
          credentials: "include",
        })
          .then((res) => {
            if (!res.ok) throw new Error("시나리오 조회 실패");
            return res.json();
          })
          .then((data) => setScenario(data))
          .catch((err) => console.error(err));
      }, []);

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
