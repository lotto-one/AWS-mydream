import React, { useRef, useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { useSelector } from "react-redux";
import { RootState } from "../store";
import { logout } from "../authSlice";
import { useDispatch } from "react-redux";
import axios from "axios";
import "./Sidebar.css";

interface SidebarProps {
  isVisible: boolean;
}

const Sidebar: React.FC<SidebarProps> = ({ isVisible }) => {
  const [imagePreview, setImagePreview] = useState<string | null>(null);
  const [username, setUsername] = useState<string | null>(null); // 사용자 이름 상태 추가
  const fileInputRef = useRef<HTMLInputElement>(null);

  const dispatch = useDispatch();

  const { rolecd } = useSelector((state: RootState) => state.auth); // Redux에서 rolecd 가져오기

  const handleLogout = () => {
    dispatch(logout());
  };

  const changeImage = () => {
    if (fileInputRef.current) {
      fileInputRef.current.click();
    }
  };

  const handleFileUpload = (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e) => {
        setImagePreview(e.target?.result as string);
      };
      reader.readAsDataURL(file);
    }
  };

  const handleImageError = (
    event: React.SyntheticEvent<HTMLImageElement, Event>
  ) => {
    event.currentTarget.src = "/img/admin_img/noimg.png"; // 대체 이미지 경로
  };

  useEffect(() => {
    const fetchUserProfile = async () => {
      const cnsno = localStorage.getItem("cnsno"); // 로컬 스토리지에서 cnsno 가져오기
      if (cnsno) {
        try {
          const response = await axios.get(
            `${process.env.REACT_APP_BACK_END_URL}/consultant/consultProfiles/${cnsno}`
          );
          setUsername(response.data.name); // API 응답에서 사용자 이름 설정
          setImagePreview(response.data.imgname);
        } catch (error) {
          console.error("사용자 정보를 불러오는 데 실패했습니다.", error);
        }
      }
    };

    fetchUserProfile();
  }, []);

  return (
    <div
      className={`sidebar ${isVisible ? "slide-enter-active" : "slide-leave-to"}`}
    >
      <div>
        <div className="my-profile devPhotoBtns btns">
          <div className="pie-chart">
            <div className="profile-img">
              <img
                src={
                  imagePreview
                    ? `http://localhost:81/yourdream/uploads/${imagePreview}`
                    : "/img/admin_img/noimge.png"
                }
                alt="Preview"
                className="preview-image"
                onError={handleImageError}
              />
            </div>
          </div>
          {/* <button
            className="profile-btn btnModify"
            type="button"
            onClick={changeImage}
          >
            <span className="skip">사진편집</span>
          </button> */}
          <input
            type="file"
            ref={fileInputRef}
            onChange={handleFileUpload}
            style={{ display: "none" }}
            accept="image/*"
          />
        </div>
        <br />
        <br />
        <h3 style={{ paddingBottom: "40px" }}>
          {username ? `${username} 님` : "로딩 중..."}
        </h3>

        {/* Conditional rendering based on rolecd */}
        {rolecd === "C" ? (
          <>
            <Link to="/consultant/consultant-profile" className="main-router">
              <p className="bar-p">개인정보 수정</p>
            </Link>
            <Link to="/consultant/feedback/main" className="main-router">
              <p className="bar-p">피드백 관리</p>
            </Link>
            <Link to="/consultant/OneToOne" className="main-router">
              <p className="bar-p">면접자 1:1 상담</p>
            </Link>
            <Link to="/consultant/Consult_Question" className="main-router">
              <p className="bar-p">직무 질문 관리</p>
            </Link>
          </>
        ) : rolecd === "A" ? (
          <>
            <Link to="/admin/User_Dash" className="main-router">
              <p className="bar-p">유저통계</p>
            </Link>
            <Link to="/admin/Consultant_Dash" className="main-router">
              <p className="bar-p">컨설턴트통계</p>
            </Link>
            <Link to="/member/MemberList" className="main-router">
              <p className="bar-p">회원 관리</p>
            </Link>
            <Link
              to="/admin-consultant/ConsultantAdmin"
              className="main-router"
            >
              <p className="bar-p">컨설턴트 관리</p>
            </Link>
          </>
        ) : null}
        <div className="sidebar-click" onClick={handleLogout}>
          <i className="bi bi-power power-icon"></i>
        </div>
      </div>
    </div>
  );
};

export default Sidebar;
