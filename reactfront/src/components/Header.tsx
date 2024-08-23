import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../store";
import { logout } from "../authSlice";
import "./Header.css";

interface HeaderProps {
  toggleSidebar: () => void;
}

const Header: React.FC<HeaderProps> = ({ toggleSidebar }) => {
  const [isHidden, setIsHidden] = useState(false);
  const dispatch = useDispatch();
  const { rolecd } = useSelector((state: RootState) => state.auth); // rolecd를 Redux 상태에서 가져옴

  const handleScroll = () => {
    setIsHidden(window.scrollY !== 0);
  };

  const handleLogout = () => {
    dispatch(logout());
  };

  useEffect(() => {
    window.addEventListener("scroll", handleScroll);
    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, []);

  // rolecd에 따라 링크를 동적으로 설정
  const profileLink =
    rolecd === "C"
      ? "/consultant/consultant-profile"
      : rolecd === "A"
        ? "/admin/User_Dash"
        : "/";

  return (
    <div className={`header ${isHidden ? "hidden" : ""}`}>
      <h1 className="header-h1">
        <Link
          to={profileLink} // 동적으로 링크 설정
          style={{ color: "black", textDecoration: "none" }}
        >
          네가, 꿈
        </Link>
        <div>
          <Link to="/" className="custom-underline" onClick={handleLogout}>
            <h5 className="header-h5" style={{ paddingRight: "20px" }}>
              로그아웃
            </h5>
          </Link>
        </div>
      </h1>
    </div>
  );
};

export default Header;
