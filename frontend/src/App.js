import React from "react";
import "./App.css";

function App() {
    return (
        <div className="container">
            {/* 햄버거 메뉴 버튼 */}
            <div className="menu-button">☰</div>

            {/* 상단 상태바 */}
            <div className="status-bar-wrapper">
                <div className="status-bar">
                    <div className="status-item">
                        <div>🗡️</div>
                        <div>공격력</div>
                        <div className="stat-value">12</div>
                    </div>
                    <div className="status-item">
                        <div>🛡️</div>
                        <div>방어력</div>
                        <div className="stat-value">8</div>
                    </div>
                    <div className="status-item">
                        <div>❤️</div>
                        <div>체력</div>
                        <div className="stat-value">85</div>
                    </div>
                    <div className="status-item">
                        <div>🔮</div>
                        <div>마나</div>
                        <div className="stat-value">50</div>
                    </div>
                    <div className="status-item">
                        <div>🧠</div>
                        <div>지능</div>
                        <div className="stat-value">14</div>
                    </div>
                    <div className="status-item">
                        <div>⚡</div>
                        <div>민첩함</div>
                        <div className="stat-value">10</div>
                    </div>
                </div>
            </div>

            {/* 본문 영역 */}
            <div className="content">
                <div className="image-box">
                    <div className="image-placeholder">[ 강아지 이미지 ]</div>
                </div>

                <div className="text-box">
                    <p>
                        당신은 한 가여운 강아지 한 마리가 엎드려 있는 것을 발견했습니다. 상태로 보아하니 오랫동안 굶은 것 같습니다.
                        불쌍한 녀석, 잡아먹을만한 부위조차 없는 것 같네요! .....가지고 있는 식량을 좀 나눠줄까요?
                    </p>
                </div>

                <div className="choices">
                    <div className="choice red">돈 식량을 좀 나눠준다</div>
                    <div className="choice green">야생의 친구 길들인다</div>
                    <div className="choice">지나간다</div>
                </div>

                <div className="page-number">– 83 –</div>
            </div>

            {/* 하단 탭 메뉴 */}
            <div className="tab-bar">
                <div className="tab">동출력</div>
                <div className="tab">응변</div>
                <div className="tab">연합뉴스</div>
            </div>
        </div>
    );
}

export default App;
