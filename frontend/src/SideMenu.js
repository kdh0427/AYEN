import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./SideMenu.css";

function SideMenu() {
    const [isOpen, setIsOpen] = useState(false);
    const navigate = useNavigate();

    // 하드코딩 사용자 정보
    const level = 12;
    const nickname = "모험가";
    const profileImageUrl = null;

    const closeMenu = () => setIsOpen(false);

    const handleProfileClick = () => {
        navigate("/user");
        closeMenu();
    };

    const handleScenarioClick = () => {
        navigate("/scenarios");
        closeMenu();
    };

    const handleAchievementClick = () => {
        navigate("/achievements");
        closeMenu();
    };

    const handleRankClick = () => {
        navigate("/rank");
        closeMenu();
    };

    const handleGoHome = () => {
        navigate("/");
        closeMenu();
    };

    return (
        <>
            {/* 메뉴 버튼 – 흐름 속에 자연스러운 위치 */}
            <div className="menu-button-inline">
                <button onClick={() => setIsOpen(true)}>☰</button>
            </div>

            {/* 오버레이 + 사이드메뉴 */}
            {isOpen && <div className="menu-overlay" onClick={closeMenu}></div>}
            <div className={`side-menu ${isOpen ? "open" : ""}`}>
                <div className="profile-section clickable" onClick={handleProfileClick}>
                    <div className="profile-image-wrapper">
                        {profileImageUrl ? (
                            <img className="profile-image" src={profileImageUrl} alt="프로필" />
                        ) : (
                            <div className="profile-placeholder"></div>
                        )}
                    </div>
                    <div className="profile-info">
                        <div className="nickname">{nickname}</div>
                        <div className="level">LV. {level}</div>
                    </div>
                </div>

                <div className="menu-section clickable" onClick={handleScenarioClick}>
                    <h3>📖 시나리오</h3>
                    <p>새로운 이야기를 선택해 보세요.</p>
                </div>

                <div className="menu-section clickable" onClick={handleAchievementClick}>
                    <h3>🏅 나의 업적</h3>
                    <p>획득한 업적을 확인해 보세요.</p>
                </div>

                <div className="menu-section clickable" onClick={handleRankClick}>
                    <h3>🏆 랭킹</h3>
                    <p>다른 유저와 비교해보세요.</p>
                </div>

                <div className="menu-section last">
                    <button className="home-button" onClick={handleGoHome}>🏠 홈으로</button>
                </div>
            </div>
        </>
    );
}

export default SideMenu;
