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
        health: 100,
        mana: 0,
        intelligence: 0,
        agility: 0
    });
    const [currentItems, setCurrentItems] = useState([]);

    const numericSceneId = Number(sceneId) || 1;
    const [currentSceneId, setCurrentSceneId] = useState(numericSceneId);
     
    useEffect(() => {
        setLoading(true);
        fetch(`http://localhost:8080/api/scenarios/${scenarioId}/scenes/${numericSceneId}`, {
            method: "GET",
            credentials: "include",
        })
            .then((res) => res.json())
            .then((res) => {
                if (res.code === 200) {
                    const { data } = res;
                    setSceneData(data);
                    setCurrentStats(prev => ({ ...prev, ...data.stats }));
                    setCurrentItems(prev => [...prev, ...(data.items || [])]);
                }
            })
            .finally(() => setLoading(false));
    }, [scenarioId, sceneId]); // ✅ 이걸로 자동 실행
    
    useEffect(() => {
        const handleClickOutside = (e) => {
            const tabWrapper = document.querySelector(".tab-bar-wrapper");
            if (selectedItem) return;
            if (showAllItems && tabWrapper && !tabWrapper.contains(e.target)) {
                setShowAllItems(false);
            }
        };
        document.addEventListener("click", handleClickOutside, true);
        return () => document.removeEventListener("click", handleClickOutside, true);
    }, [showAllItems, selectedItem]);

    const isConditionMet = (condition, stats, requiredItem, currentItems) => {
        if (condition && condition.intelligence && stats.intelligence < condition.intelligence) return false;
        if (requiredItem) {
            return currentItems.some(item => item.name === requiredItem);
        }
        return true;
    };

    const applyEffect = (effect, prevStats, prevItems) => {
        if (!effect) return { newStats: prevStats, newItems: prevItems };
    
        const changes = {
            health: effect.health || 0,
            attack: effect.attack || 0,
            defense: effect.defense || 0,
            mana: effect.mana || 0,
            intelligence: effect.intelligence || 0,
            agility: effect.agility || 0
        };
    
        const newStats = {
            attack: prevStats.attack + changes.attack,
            defense: prevStats.defense + changes.defense,
            health: prevStats.health + changes.health,
            mana: prevStats.mana + changes.mana,
            intelligence: prevStats.intelligence + changes.intelligence,
            agility: prevStats.agility + changes.agility
        };
    
        let newItems = prevItems;
        if (effect.addItem) {
            if (!prevItems.some(i => i.name === effect.addItem.name)) {
                newItems = [...prevItems, effect.addItem];
            }
        }
    
        setStatChanges(changes);
        setTimeout(() => setStatChanges({}), 1000);
    
        return { newStats, newItems };
    };    

    const handleChoice = async (choice) => {
        if (!isConditionMet(choice.required_condition, currentStats, choice.required_item, currentItems)) return;
    
        const roleName = choice.description;
    
        // 서버에 시작 정보 등록
        if(numericSceneId === 1){
            await fetch(`http://localhost:8080/api/scenarios/${scenarioId}/scenes/${currentSceneId}`, {
                method: "POST",
                credentials: "include",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ role: roleName }) // ← body로 전송
            });
        }

        const { newStats, newItems } = applyEffect(choice.effect, currentStats, currentItems);

        // 상태 업데이트
        setCurrentStats(newStats);
        setCurrentItems(newItems);

        await fetch("http://localhost:8080/api/choose", {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                effect: newStats || {},
                item: newItems.find(i => i.name === choice.effect.addItem?.name) || {}
            })
        });
  
        setPageNumber(prev => prev + 1);
        const nextSceneId = choice.nextSceneId;
        setCurrentSceneId(nextSceneId);
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

    const { content, image_url, choices } = sceneData;

    return (
        <div className="container">
            <SideMenu />
            <div className="status-bar-wrapper">
                <div className="status-bar">
                    {[
                        { icon: "🗡️", label: "공격력", key: "attack" },
                        { icon: "🛡️", label: "방어력", key: "defense" },
                        { icon: "❤️", label: "체력", key: "health" },
                        { icon: "🔮", label: "마나", key: "mana" },
                        { icon: "🧠", label: "지능", key: "intelligence" },
                        { icon: "⚡", label: "민첩함", key: "agility" }
                    ].map(({ icon, label, key }) => (
                        <div className="status-item" key={key}>
                            <div>{icon}</div>
                            <div>{label}</div>
                            <div className="stat-value">
                                {currentStats[key]}
                                {statChanges[key] ? (
                                    <span className={`stat-change ${statChanges[key] > 0 ? "up" : "down"}`}>
                                        {statChanges[key] > 0 ? `+${statChanges[key]}` : statChanges[key]}
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
                            src={image_url}
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
                        const disabled = !isConditionMet(choice.required_condition, currentStats, choice.required_item, currentItems);
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
                    {(showAllItems ? currentItems : currentItems.slice(0, 3)).map((item, idx) => (
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
                    ))}
                </div>
            </div>

            {selectedItem && (
                <div className="item-tooltip-backdrop" onClick={() => setSelectedItem(null)}>
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
