import React, { useEffect, useState } from "react";
import "./Rank.css";
import SideMenu from "./SideMenu";

function Rank() {
    const [rankings, setRankings] = useState([]);
    const [loading, setLoading] = useState(true);
    const [errorMsg, setErrorMsg] = useState("");

    useEffect(() => {
        setLoading(true);

        // 🔽 실제 API 사용 시 이 부분을 주석 해제
        /*
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
        */

        // 🔽 테스트용 더미 데이터
        setTimeout(() => {
            const mockResponse = {
                code: 200,
                data: [
                    { name: "용감한 토끼", level: 12, achievement_count: 18 },
                    { name: "지혜로운 여우", level: 11, achievement_count: 16 },
                    { name: "빛의 검사", level: 11, achievement_count: 14 },
                    { name: "모험가", level: 10, achievement_count: 10 },
                    { name: "어둠의 기사", level: 9, achievement_count: 8 },
                    { name: "나는 김동현", level: 88, achievement_count: 8 },
                ],
                msg: "200 ok"
            };

            if (mockResponse.code === 200) {
                const sortedData = mockResponse.data.sort((a, b) => b.level - a.level);
                setRankings(sortedData);
            } else if (mockResponse.code === 204) {
                setRankings([]);
                setErrorMsg("데이터가 존재하지 않습니다.");
            } else {
                setErrorMsg("랭킹 데이터를 불러오는 데 실패했습니다.");
            }

            setLoading(false);
        }, 1000);
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
