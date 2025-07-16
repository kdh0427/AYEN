import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import SideMenu from "./SideMenu";
import "./Story.css";

function Story() {
  const { scenarioId, sceneId } = useParams();
  const navigate = useNavigate();

  const [sceneData, setSceneData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [imageError, setImageError] = useState(false);
  const [showAllItems, setShowAllItems] = useState(false);
  const [selectedItem, setSelectedItem] = useState(null);
  const [pageNumber, setPageNumber] = useState(1);
  const [statChanges, setStatChanges] = useState({});
  const [currentStats, setCurrentStats] = useState({
    attack: 0,
    defense: 0,
    health: 50,
  });
  const [currentItems, setCurrentItems] = useState([]);

  const defaultSceneId = scenarioId === "2" ? 12 : 1;
  const numericSceneId = Number(sceneId) || defaultSceneId;

  useEffect(() => {
    setLoading(true);
    fetch(
      `http://localhost:8080/api/scenarios/${scenarioId}/scenes/${numericSceneId}`,
      {
        method: "GET",
        credentials: "include",
      }
    )
      .then((res) => res.json())
      .then((res) => {
        if (res.code === 200) {
          const { data } = res;
          setSceneData(data);
          console.log(data.stats);
          setCurrentStats((prev) => ({ ...prev, ...data.stats }));
          setCurrentItems((prev) => [...prev, ...(data.items || [])]);
        }
      })
      .finally(() => setLoading(false));
  }, [scenarioId, numericSceneId]); // ✅ 이걸로 자동 실행

  useEffect(() => {
    const handleClickOutside = (e) => {
      const tabWrapper = document.querySelector(".tab-bar-wrapper");
      if (selectedItem) return;
      if (showAllItems && tabWrapper && !tabWrapper.contains(e.target)) {
        setShowAllItems(false);
      }
    };
    document.addEventListener("click", handleClickOutside, true);
    return () =>
      document.removeEventListener("click", handleClickOutside, true);
  }, [showAllItems, selectedItem]);

  const isConditionMet = (condition, stats, requiredItem, currentItems) => {
    if (
      condition &&
      condition.intelligence &&
      stats.intelligence < condition.intelligence
    )
      return false;
    if (requiredItem) {
      return currentItems.some((item) => item.name === requiredItem);
    }
    return true;
  };

  const applyEffect = (effect, prevStats, prevItems) => {
    if (!effect) return { newStats: prevStats, newItems: prevItems };

    const changes = {
      health: effect.health || 0,
      attack: effect.attack || 0,
      defense: effect.defense || 0,
    };

    const newStats = {
      attack: prevStats.attack + changes.attack,
      defense: prevStats.defense + changes.defense,
      health: prevStats.health + changes.health,
    };

    let newItems = prevItems;
    if (effect.addItem && Array.isArray(effect.addItem)) {
      const newUniqueItems = effect.addItem.filter(
        (item) => !prevItems.some((i) => i.name === item.name)
      );
      newItems = [...prevItems, ...newUniqueItems];
    }

    setStatChanges(changes);
    setTimeout(() => setStatChanges({}), 1000);

    return { newStats, newItems };
  };

  const handleChoice = async (choice) => {
    if (
      !isConditionMet(
        choice.required_condition,
        currentStats,
        choice.required_item,
        currentItems
      )
    )
      return;

    const roleName = choice.description;

    // 서버에 시작 정보 등록
    if (numericSceneId === 1) {
      await fetch(
        `http://localhost:8080/api/scenarios/${scenarioId}/scenes/${numericSceneId}`,
        {
          method: "POST",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ role: roleName }), // ← body로 전송
        }
      );
    }

    let requiredItem = choice.requiredItem;
    if (typeof requiredItem === "string") {
      try {
        requiredItem = JSON.parse(requiredItem); // 문자열이면 객체로 변환
      } catch (e) {
        console.error("requiredItem JSON 파싱 실패", e);
        requiredItem = {}; // fallback
      }
    }

    try {
      const response = await fetch("http://localhost:8080/api/itemCheck", {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          requiredItem: requiredItem,
        }),
      });

      if (!response.ok) {
        // 서버 응답을 JSON으로 파싱 (가능할 경우)
        let result = {};
        try {
          result = await response.json();
        } catch (e) {
          console.warn("응답 JSON 파싱 실패", e);
        }

        // 사용자에게만 부드럽게 안내
        alert(result.message || "이 선택지는 사용할 수 없습니다.");
        return;
      }

      // 정상 처리...
    } catch (error) {
      // 네트워크 등 기타 에러만 콘솔에 출력
      console.error("요청 중 오류 발생:", error);
      alert("요청에 실패했습니다. 네트워크 상태를 확인하세요.");
    }

    const { newStats, newItems } = applyEffect(
      choice.effect,
      currentStats,
      currentItems
    );

    // 상태 업데이트
    setCurrentStats(newStats);
    setCurrentItems(newItems);

    await fetch("http://localhost:8080/api/choose", {
      method: "POST",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        effect: newStats || {},
        item: choice.effect.addItem || [],
      }),
    });

    const updatedScene = await fetch(
      `http://localhost:8080/api/scenarios/${scenarioId}/scenes/${numericSceneId}`,
      {
        method: "GET",
        credentials: "include",
      }
    );

    setPageNumber((prev) => prev + 1);
    const updatedData = await updatedScene.json();
    const updatedChoice = updatedData.data.choices.find(
      (c) => c.description === choice.description
    );
    const nextSceneId = updatedChoice?.nextSceneId ?? choice.nextSceneId;

    if (scenarioId === "2" && numericSceneId === 1) {
      navigate(`/scenarios/2/scenes/13`);
      return;
    }
    
    if (nextSceneId === 1) {
      navigate("/scenarios"); // 메인 화면 경로로 변경
      return; // 더 이상 진행하지 않도록 return
    }
    navigate(`/scenarios/${scenarioId}/scenes/${nextSceneId}`);
  };

  if (loading || !sceneData) {
    return (
      <div className="container">
        <SideMenu />
        <div className="loading-text">로딩 중...</div>
      </div>
    );
  }

  const { content, imageUrl, choices } = sceneData;

  return (
    <div className="container">
      <div className="top-bar">
        <SideMenu />
        <div className="logo">📖 AYEN</div>
      </div>
      <div className="status-bar-wrapper">
        <div className="status-bar">
          {[
            { icon: "🗡️", label: "공격력", key: "attack" },
            { icon: "🛡️", label: "방어력", key: "defense" },
            { icon: "❤️", label: "체력", key: "health" },
          ].map(({ icon, label, key }) => (
            <div className="status-item" key={key}>
              <div>{icon}</div>
              <div>{label}</div>
              <div className="stat-value">
                {currentStats[key]}
                {statChanges[key] ? (
                  <span
                    className={`stat-change ${
                      statChanges[key] > 0 ? "up" : "down"
                    }`}
                  >
                    {statChanges[key] > 0
                      ? `+${statChanges[key]}`
                      : statChanges[key]}
                  </span>
                ) : null}
              </div>
            </div>
          ))}
        </div>
      </div>

      <div className="content">
        <div className="image-box">
          {imageError ? (
            <div className="image-placeholder">이미지</div>
          ) : (
            <img
              src={imageUrl}
              alt="scene"
              className="scene-image"
              onError={() => setImageError(true)}
            />
          )}
        </div>

        <div className="text-box">
          <p>{content}</p>
        </div>

        <div className="choices">
          {choices.map((choice, index) => {
            const disabled = !isConditionMet(
              choice.required_condition,
              currentStats,
              choice.required_item,
              currentItems
            );
            return (
              <div
                key={index}
                className={`choice ${disabled ? "disabled-choice" : ""}`}
                onClick={() => {
                  if (!disabled) handleChoice(choice);
                }}
              >
                {choice.description}
                {choice.required_condition && (
                  <span className="condition-text">
                    (조건: 지능 {choice.required_condition.intelligence})
                  </span>
                )}
                {choice.required_item && (
                  <span className="condition-text">
                    (필요 아이템: {choice.required_item})
                  </span>
                )}
              </div>
            );
          })}
        </div>

        <div style={{ flexGrow: 1 }} />
        <div className="page-number">– {pageNumber} –</div>
      </div>

      <div className="tab-bar-wrapper">
        <div
          className={`tab-bar ${showAllItems ? "expanded" : "collapsed"}`}
          onClick={() => {
            if (!showAllItems) {
              setTimeout(() => setShowAllItems(true), 0);
            }
          }}
        >
          {(showAllItems ? currentItems : currentItems.slice(0, 3)).map(
            (item, idx) => (
              <div
                key={idx}
                className="tab"
                onClick={(e) => {
                  if (!showAllItems) return;
                  e.stopPropagation();
                  setSelectedItem(item);
                }}
              >
                {item.name}
              </div>
            )
          )}
        </div>
      </div>

      {selectedItem && (
        <div
          className="item-tooltip-backdrop"
          onClick={() => setSelectedItem(null)}
        >
          <div className="item-tooltip" onClick={(e) => e.stopPropagation()}>
            <h4>{selectedItem.name}</h4>
            <p>{selectedItem.description}</p>
          </div>
        </div>
      )}
    </div>
  );
}

export default Story;
