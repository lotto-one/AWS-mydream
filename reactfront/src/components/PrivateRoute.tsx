import React from "react";
import { Navigate, useLocation } from "react-router-dom";
import { useSelector } from "react-redux";
import { RootState } from "../store";

interface PrivateRouteProps {
  element: React.ReactElement;
  roleRequired?: string; // 특정 역할이 필요한 경우
  redirectTo?: string; // 토큰이 있을 때 리디렉션할 경로
}

const PrivateRoute: React.FC<PrivateRouteProps> = ({
  element,
  roleRequired,
  redirectTo = "/main/login", // 기본 리디렉션 경로
}) => {
  const { token, rolecd } = useSelector((state: RootState) => state.auth);
  const location = useLocation();

  if (!token) {
    // 로그인되지 않은 사용자는 로그인 페이지로 리디렉션
    return <Navigate to={redirectTo} replace />;
  }

  if (roleRequired && rolecd !== roleRequired) {
    // 역할이 맞지 않으면 비정상적인 접근 메시지를 표시하고 이전 페이지로 돌아가기
    alert("비정상적인 접근입니다. 페이지를 다시 확인해 주세요.");
    window.history.back(); // 이전 페이지로 돌아가기
    return null; // `window.history.back()`은 즉시 이동하기 때문에 `return null`을 추가하여 컴포넌트가 더 이상 렌더링되지 않도록 합니다
  }

  return element;
};

export default PrivateRoute;
