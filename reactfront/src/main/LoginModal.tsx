import React, { useState } from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { login } from "../authSlice";
import { unwrapResult } from "@reduxjs/toolkit";
import "./LoginModal.css";

interface LoginModalProps {
  isOpen: boolean;
  onClose: () => void;
  loginType: "admin" | "consul";
}

const LoginModal: React.FC<LoginModalProps> = ({
  isOpen,
  onClose,
  loginType,
}) => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);

  const handleLogin = async (event: React.FormEvent) => {
    event.preventDefault();
    setError(null);

    try {
      // Thunk 액션 디스패치
      const resultAction = await dispatch(login({ email, password }) as any);

      // unwrapResult로 결과를 명확하게 추출합니다
      const result = unwrapResult(resultAction);
      console.log(result); // result를 콘솔에 출력하여 확인

      const { rolecd } = result;

      if (loginType === "admin") {
        if (rolecd === "A") {
          navigate("/admin/User_Dash");
        } else {
          alert("관리자 전용 페이지입니다. 관리자 계정으로 로그인하세요.");
          localStorage.clear(); // 로컬스토리지 초기화
          setError("관리자 전용 페이지입니다.");
        }
      } else if (loginType === "consul") {
        if (rolecd === "C") {
          navigate("/consultant/consultant-profile");
        } else {
          alert("컨설턴트 전용 페이지입니다. 컨설턴트 계정으로 로그인하세요.");
          localStorage.clear(); // 로컬스토리지 초기화
          setError("컨설턴트 전용 페이지입니다.");
        }
      }

      // 로그인 성공 후 모달 닫기
      if (rolecd === "A" || rolecd === "C") {
        onClose();
      }
    } catch (error) {
      console.error(error);
      setError("로그인 실패. 자격 증명을 확인하세요.");
    }
  };

  if (!isOpen) return null;

  return (
    <div className="login-modal-overlay">
      <div className="login-modal">
        <h2 className="loginform-h2">
          {loginType === "admin" ? "관리자 로그인" : "컨설턴트 로그인"}
        </h2>
        <form className="login-form" onSubmit={handleLogin}>
          <label htmlFor="email">아이디</label>
          <input
            type="email"
            id="email"
            name="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />

          <label htmlFor="password">비밀번호</label>
          <input
            type="password"
            id="password"
            name="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />

          <button type="submit" className="submit-button">
            로그인
          </button>
          <button type="button" className="cancel-button" onClick={onClose}>
            취소
          </button>
        </form>
        {error && <p className="login-error">{error}</p>}
      </div>
    </div>
  );
};

export default LoginModal;
