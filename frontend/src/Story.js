import React, { useEffect, useState } from "react";
import SideMenu from "./SideMenu";
import "./Story.css";

function Story() {
    const [sceneData, setSceneData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [imageError, setImageError] = useState(false);
    const [showAllItems, setShowAllItems] = useState(false);
    const [selectedItem, setSelectedItem] = useState(null);
    const [currentSceneId, setCurrentSceneId] = useState(1);
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

    const sceneMap = {
        1: {
            content: "당신의 직업을 선택하세요.",
            image_url: "https://cdn.ayen.app/images/class.jpg",
            is_ending: false,
            choices: [
                {
                    description: "전사",
                    next_scene_id: 2,
                    required_condition: null,
                    effect: {},
                    professionStats: { attack: 12, defense: 10, health: 120, mana: 10, intelligence: 5, agility: 5 },
                    professionItems: [
                        { name: "강철 검", description: "공격력 +10", effect: 1 },
                        { name: "회복 포션", description: "체력을 30 회복합니다.", effect: 2 }
                    ]
                },
                {
                    description: "마법사",
                    next_scene_id: 2,
                    required_condition: null,
                    effect: {},
                    professionStats: { attack: 5, defense: 5, health: 80, mana: 50, intelligence: 15, agility: 5 },
                    professionItems: [
                        { name: "불꽃봉", description: "마법 공격력 +15", effect: 3 },
                        { name: "마력 포션", description: "마나 +30", effect: 4 }
                    ]
                },
                {
                    description: "도적",
                    next_scene_id: 2,
                    required_condition: null,
                    effect: {},
                    professionStats: { attack: 8, defense: 6, health: 90, mana: 20, intelligence: 10, agility: 15 },
                    professionItems: [
                        { name: "단검", description: "민첩함 +10", effect: 5 },
                        { name: "연막탄", description: "적의 시야를 가립니다.", effect: 6 }
                    ]
                }
            ],
            items: [],
            stats: {}
        },
        2: {
            content: "모험이 시작되었습니다. 앞에 숲이 보입니다.",
            image_url: "https://cdn.ayen.app/images/scene1.jpg",
            is_ending: false,
            choices: [
                {
                    description: "숲으로 들어간다.",
                    next_scene_id: 3,
                    required_condition: null,
                    effect: { agility: 1 }
                },
                {
                    description: "다른 길을 찾는다.",
                    next_scene_id: 3,
                    required_condition: null,
                    effect: { intelligence: 1 }
                }
            ],
            items: [],
            stats: {}
        },
        3: {
            content: "작은 괴물을 만났습니다!",
            image_url: "https://cdn.ayen.app/images/scene2.jpg",
            is_ending: false,
            choices: [
                {
                    description: "싸운다",
                    next_scene_id: 4,
                    required_condition: null,
                    effect: { health: -10, attack: 2 }
                },
                {
                    description: "도망친다",
                    next_scene_id: 4,
                    required_condition: null,
                    effect: { agility: 1 }
                }
            ],
            items: [],
            stats: {}
        },
        4: {
            content: "모닥불을 발견하고 잠시 휴식을 취합니다.",
            image_url: "https://cdn.ayen.app/images/scene3.jpg",
            is_ending: false,
            choices: [
                {
                    description: "잠시 쉰다",
                    next_scene_id: 5,
                    required_condition: null,
                    effect: { health: 10 }
                },
                {
                    description: "주변을 살핀다",
                    next_scene_id: 5,
                    required_condition: null,
                    effect: { intelligence: 1 }
                }
            ],
            items: [],
            stats: {}
        },
        5: {
            content: "산 속 동굴 입구에 도착했습니다.",
            image_url: "https://cdn.ayen.app/images/scene4.jpg",
            is_ending: false,
            choices: [
                {
                    description: "동굴로 들어간다",
                    next_scene_id: 6,
                    required_condition: null,
                    effect: { intelligence: 1 }
                },
                {
                    description: "주위를 탐색한다",
                    next_scene_id: 6,
                    required_condition: null,
                    effect: { agility: 1 }
                }
            ],
            items: [],
            stats: {}
        },
        6: {
            content: "동굴 안에서 빛나는 보석을 발견했습니다!",
            image_url: "https://cdn.ayen.app/images/scene5.jpg",
            is_ending: false,
            choices: [
                {
                    description: "보석을 가져간다",
                    next_scene_id: 7,
                    required_condition: null,
                    effect: {
                        mana: 10,
                        addItem: { name: "빛나는 보석", description: "마력을 품은 보석입니다.", effect: null }
                    }
                },
                {
                    description: "그냥 둔다",
                    next_scene_id: 7,
                    required_condition: null,
                    effect: {}
                }
            ],
            items: [],
            stats: {}
        },
        7: {
            content: "거대한 문이 앞을 막고 있습니다. 안에는 무언가 있습니다...",
            image_url: "https://cdn.ayen.app/images/scene6.jpg",
            is_ending: false,
            choices: [
                {
                    description: "빛나는 보석으로 문을 연다",
                    next_scene_id: 8,
                    required_condition: null,
                    required_item: "빛나는 보석", // 🔑 아이템 조건 추가
                    effect: { attack: 1 }
                },
                {
                    description: "열쇠를 찾아본다",
                    next_scene_id: 8,
                    required_condition: null,
                    effect: { intelligence: 2 }
                }
            ],
            items: [],
            stats: {}
        },
        8: {
            content: "드디어 보스를 만났습니다!",
            image_url: "https://cdn.ayen.app/images/scene7.jpg",
            is_ending: true,
            choices: [
                {
                    description: "보스를 물리치고 다시 시작한다",
                    next_scene_id: 1,
                    required_condition: null,
                    effect: { health: -20, attack: 5 }
                }
            ],
            items: [
                { name: "보스의 검", description: "강력한 힘이 깃든 검입니다.", effect: null }
            ],
            stats: { attack: 3, health: 10 }
        }
    };

    useEffect(() => {
        setLoading(true);

        // ✅ 실제 API 요청 (현재는 주석 처리)
        /*
        fetch(`/api/scenarios/1/scenes/${currentSceneId}`, {
            method: "GET",
            headers: {
                Authorization: `Bearer ${yourToken}`
            }
        })
            .then(res => res.json())
            .then(res => {
                if (res.code === 200) {
                    const { data } = res;
                    setCurrentStats(data.stats);
                    setCurrentItems(data.items);
                    setSceneData(data);
                }
                setLoading(false);
            })
            .catch(() => setLoading(false));
        */

        // 🔧 로컬 더미 데이터 사용
        const mock = sceneMap[currentSceneId];

        if (currentSceneId === 2 && sceneData === null) {
            const storedChoice = sceneMap[1].choices.find(c => c.next_scene_id === 2 && c.professionStats);
            if (storedChoice) {
                setCurrentStats(storedChoice.professionStats);
                setCurrentItems(storedChoice.professionItems);
            }
        }

        setSceneData({
            ...mock,
            items: [...currentItems, ...(mock.items || [])],
            stats: { ...currentStats, ...(mock.stats || {}) }
        });
        setLoading(false);
    }, [currentSceneId]);

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
        if (condition) {
            if (condition.intelligence && stats.intelligence < condition.intelligence) return false;
            // 다른 능력 조건들도 여기에 추가 가능
        }
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

        // 능력치 반영
        setCurrentStats((prev) => ({
            ...prev,
            health: prev.health + changes.health,
            attack: prev.attack + changes.attack,
            defense: prev.defense + changes.defense,
            mana: prev.mana + changes.mana,
            intelligence: prev.intelligence + changes.intelligence,
            agility: prev.agility + changes.agility
        }));

        // 새로운 아이템 반영
        if (effect.addItem) {
            setCurrentItems((prev) => [...prev, effect.addItem]);
        }

        // 잠깐 보여줄 스탯 변경값
        setStatChanges(changes);
        setTimeout(() => setStatChanges({}), 1000);
    };

    const handleChoice = (choice) => {
        if (!isConditionMet(choice.required_condition, currentStats, choice.required_item, currentItems)) return;

        if (choice.professionStats) {
            setCurrentStats(choice.professionStats);
            setCurrentItems(choice.professionItems);
        } else {
            applyEffect(choice.effect);
        }

        const nextScene = sceneMap[choice.next_scene_id];

        if (nextScene.items && nextScene.items.length > 0) {
            setCurrentItems(prev => [...prev, ...nextScene.items]);
        }

        // ✅ 선택지 결과를 서버에 전송하는 API (현재는 주석 처리)
        /*
        fetch("/api/choose", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${yourToken}`
            },
            body: JSON.stringify({
                effect: {
                    health: choice.effect?.health || 0,
                    attack: choice.effect?.attack || 0,
                    defense: choice.effect?.defense || 0,
                    mana: choice.effect?.mana || 0,
                    intelligence: choice.effect?.intelligence || 0,
                    agility: choice.effect?.agility || 0,
                    item_changes: [] // 필요 시 아이템 변화 정보도 같이 전송
                }
            })
        })
            .then(res => res.json())
            .then(res => {
                if (res.code !== 201) {
                    console.error("선택 처리 실패:", res.msg);
                }
            });
        */

        setCurrentSceneId(choice.next_scene_id);
        setPageNumber(prev => prev + 1);
    };

    if (loading || !sceneData) {
        return (
            <div className="container">
                <SideMenu />
                <div className="loading-text">로딩 중...</div>
            </div>
        );
    }

    const { content, image_url, choices, items } = sceneData;

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
                    {(showAllItems ? items : items.slice(0, 3)).map((item, idx) => (
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
