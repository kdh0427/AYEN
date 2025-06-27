import { useLocation } from "react-router-dom";
import SideMenu from "./SideMenu";
import "./RecordDetail.css";

function RecordDetail() {
    const { state } = useLocation();

    if (!state) {
        return (
            <div className="record-page">
                <SideMenu />
                <div className="record-error">
                    <h2>❌ 잘못된 접근입니다</h2>
                    <p>리스트에서 항목을 클릭해 상세 내용을 확인하세요.</p>
                </div>
            </div>
        );
    }

    const { title, description, image_url, achievedAt, exp, type } = state;

    return (
        <div className="record-page">
            <SideMenu />

            <div className="record-detail-body">
                <h1 className="record-title">{title}</h1>

                <div className="record-image-placeholder" />

                <p className="record-description">{description}</p>

                <div className="record-meta">
                    <p><strong>달성일:</strong> {achievedAt}</p>
                    <p><strong>획득 경험치:</strong> {exp} XP</p>
                </div>
            </div>
        </div>
    );
}

export default RecordDetail;
