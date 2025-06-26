import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./ScenarioList.css";
import SideMenu from "./SideMenu";

/*
const scenarios = [
    { id: 1, title: "기억의 숲", description: "깨어나보니 숲 한가운데. 잃어버린 기억을 찾아야 합니다.", imageUrl: null },
    { id: 2, title: "도심 속의 늑대", description: "도시의 그림자 속, 당신은 진실을 추적합니다.", imageUrl: null },
    { id: 3, title: "마지막 승객", description: "종착역을 향해 달리는 기차, 단 한 명만이 살아남습니다.", imageUrl: null },
    { id: 4, title: "붉은 달의 전설", description: "붉은 달이 뜨는 밤, 저주받은 성으로 향합니다.", imageUrl: null },
    { id: 5, title: "잊혀진 실험실", description: "문이 잠긴 연구소, 비밀이 잠들어 있습니다.", imageUrl: null }
];
*/

function ScenarioList({ onMenuClick }) {
    const[scenarios, setScenario] = useState([]);
    const navigate = useNavigate();

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
        navigate("/story");
    };

    return (
        <div className="scenario-page">
            <div className="top-bar">
                <SideMenu />
            </div>
            <h2 className="scenario-title">시나리오 선택</h2>
            <div className="scenario-grid">
                {scenarios.map((s) => (
                    <div key={s.id} className="scenario-card" onClick={() => handleScenarioClick(s.id)}>
                        <div className="scenario-image" />
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
