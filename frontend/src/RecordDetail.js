import { useLocation, useNavigate } from "react-router-dom";
import SideMenu from "./SideMenu";
import { format } from "date-fns";
import "./RecordDetail.css";

function RecordDetail() {
  const { state } = useLocation();
  const navigate = useNavigate();

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

  const { title, description, image_url, achieved_at, exp, cnt } = state;

  return (
    <div className="record-page">
      <div className="top-bar">
        <SideMenu />
        <div className="logo">📖 AYEN</div>
      </div>
      <div className="record-detail-body">
        <button className="back-button" onClick={() => navigate(-1)}>
          돌아가기
        </button>
        <h1 className="record-title">{title}</h1>

        <img src={image_url} alt={title} className="record-image" />

        <p className="record-description">{description}</p>

        <div className="record-meta">
          <p>
            <strong>달성일 : </strong>{" "}
            {format(new Date(achieved_at), "yyyy-MM-dd HH:mm")}
          </p>
          <p>
            <strong>획득 경험치 : </strong> {exp} XP
          </p>
          {state.type === "결말" && (
            <p>
              <strong>달성 횟수 : </strong> {cnt} 회
            </p>
          )}
        </div>
      </div>
    </div>
  );
}

export default RecordDetail;
