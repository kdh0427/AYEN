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
                const res = await fetch("http://localhost:8080/api/scenarios", {
                    method: "GET",
                    credentials: "include", // 쿠키 포함 (세션 유지)
                });
                const data = await res.json();
                setScenarios(data);
            } catch (error) {
                console.error("Error fetching scenarios:", error);
                setErrorMsg("서버 오류가 발생했습니다.");
            } finally {
                setLoading(false);
            }
        };

        fetchScenarios();
    }, []);

    const handleScenarioClick = async (scenarioId) => {
        const confirmed = window.confirm("이 시나리오를 선택하시겠습니까?");
        if (!confirmed) return;
      
        try {
          // 템플릿 리터럴을 백틱으로 감싸야 변수 치환됨
          const response = await fetch(`http://localhost:8080/api/scenarios/${scenarioId}/scenes`, {
            method: "POST",
            credentials: "include",
          });
      
          console.log(response);
          if (!response.ok) {
            throw new Error("신 번호를 가져오는데 실패했습니다.");
          }
      
          // 응답이 JSON이라고 가정
          const data = await response.json();
      
          // 서버가 { lastSceneId: 3 } 이런 식으로 준다고 가정
          const lastSceneId = data.lastSceneId || 1; // 기본값 1
      
          // 해당 마지막 신 번호로 네비게이트
          navigate(`/scenarios/${scenarioId}/scenes/${lastSceneId}`);
      
        } catch (error) {
          console.error(error);
          alert("신 정보를 가져오는 중 오류가 발생했습니다.");
        }
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
