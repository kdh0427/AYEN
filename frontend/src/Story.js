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
        const updatedSceneId = Number(sceneId) || 1; // ✅ 여기서 다시 계산
        setLoading(true);
        fetch(`http://localhost:8080/api/scenarios/${scenarioId}/scenes/${updatedSceneId}`, {
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

    const applyEffect = (effect) => {
        if (!effect) return;
    
        const changes = {
            health: effect.health || 0,
            attack: effect.attack || 0,
            defense: effect.defense || 0,
            mana: effect.mana || 0,
            intelligence: effect.intelligence || 0,
            agility: effect.agility || 0
        };
    
        setCurrentStats(prev => ({
            ...prev,
            attack: prev.attack + changes.attack,
            defense: prev.defense + changes.defense,
            health: prev.health + changes.health,
            mana: prev.mana + changes.mana,
            intelligence: prev.intelligence + changes.intelligence,
            agility: prev.agility + changes.agility
        }));
    
        if (effect.addItem) {
            setCurrentItems(prev => {
                if (!prev.some(i => i.name === effect.addItem.name)) {
                    return [...prev, effect.addItem];
                }
                return prev;
            });
        }
    
        setStatChanges(changes);
        setTimeout(() => setStatChanges({}), 1000);
    };    

    const handleChoice = async (choice) => {
        if (!isConditionMet(choice.required_condition, currentStats, choice.required_item, currentItems)) return;
    
        const roleName = choice.description;
        const numericSceneId = Number(sceneId) || 1;
    
        if (choice.professionStats) {
            setCurrentStats(choice.professionStats);
            setCurrentItems(choice.professionItems);
    
            // 서버에 시작 정보 등록
            await fetch(`http://localhost:8080/api/scenarios/${scenarioId}/scenes/${numericSceneId}?role=${encodeURIComponent(roleName)}`, {
                method: "POST",
                credentials: "include"
            });
    
            // 선택 결과 전송
            await fetch("/api/choose", {
                method: "POST",
                headers: {
                  "Content-Type": "application/json"
                },
                body: JSON.stringify({
                  effect: choice.professionStats || {},
                  item_changes: choice.professionItems || []
                })
            });
        } else {
            applyEffect(choice.effect);
        }
    
        setPageNumber(prev => prev + 1);
        navigate(`/scenarios/${scenarioId}/scenes/${choice.next_scene_id}`); // 이게 자동으로 useEffect를 트리거
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
