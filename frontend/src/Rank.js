import React, { useEffect, useState } from "react";
import "./Rank.css";
import SideMenu from "./SideMenu";

function Rank() {
    const [rankings, setRankings] = useState([]);
    const [loading, setLoading] = useState(true);
    const [errorMsg, setErrorMsg] = useState("");

    useEffect(() => {
        setLoading(true);

        fetch("/api/rankings")
            .then((res) => res.json())
            .then((response) => {
                if (response.code === 200) {
                    const sortedData = response.data.sort((a, b) => b.level - a.level);
                    setRankings(sortedData);
                } else if (response.code === 204) {
                    setRankings([]);
                    setErrorMsg("데이터가 존재하지 않습니다.");
                } else {
                    setErrorMsg("랭킹 데이터를 불러오는 데 실패했습니다.");
                }
            })
            .catch(() => {
                setErrorMsg("서버와의 통신에 실패했습니다.");
            })
            .finally(() => setLoading(false));
    }, []);

    return (
        <div className="rank-page">
            <SideMenu />
            <h2 className="rank-title">🏆 랭킹</h2>
            <p className="rank-subtitle">상위 유저들과 나의 위치를 확인해 보세요!</p>

            {loading ? (
                <p className="loading-text">로딩 중...</p>
            ) : errorMsg ? (
                <p className="error-text">{errorMsg}</p>
            ) : (
                <ul className="rank-list">
                    {rankings.map((user, index) => (
                        <li key={index} className={`rank-item ${user.name === "모험가" ? "me" : ""}`}>
                            <span className="rank-position">#{index + 1}</span>
                            <span className="rank-name">{user.name}</span>
                            <span className="rank-level">LV. {user.level}</span>
                            <span className="rank-achieve">🏅 {user.achievement_count}개</span>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}

export default Rank;
