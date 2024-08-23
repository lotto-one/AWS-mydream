// src/pages/Login.tsx
import React, { useState } from "react";
import LoginModal from "../main/LoginModal";
import "./Login.css"; // CSS 파일을 불러옵니다

const Login: React.FC = () => {
  // 빈 문자열과 'admin' 또는 'consul' 값을 허용하는 타입으로 수정
  const [modalIsOpen, setModalIsOpen] = useState(false);
  const [loginType, setLoginType] = useState<"admin" | "consul" | "">("");

  const openModal = (type: "admin" | "consul") => {
    setLoginType(type);
    setModalIsOpen(true);
  };

  const closeModal = () => {
    setModalIsOpen(false);
    setLoginType(""); // 빈 문자열로 초기화
  };

  return (
    <div className="login-container">
      <h1 className="login-h1">내, 가꿈</h1>
      <div className="login-box-container">
        <div className="login-admin">
          <h2>관리자 로그인</h2>
          <button className="login-button" onClick={() => openModal("admin")}>
            로그인
          </button>
        </div>
        <div className="login-consul">
          <h2>컨설턴트 로그인</h2>
          <button className="login-button" onClick={() => openModal("consul")}>
            로그인
          </button>
        </div>
      </div>

      <LoginModal
        isOpen={modalIsOpen}
        onClose={closeModal}
        loginType={loginType as "admin" | "consul"} // loginType을 올바르게 전달
      />
    </div>
  );
};

export default Login;
