import React, { useState } from "react";
import {
  BrowserRouter as Router,
  Route,
  Routes,
  Navigate,
} from "react-router-dom";
import Footer from "./components/Footer";
import Header from "./components/Header";
import Sidebar from "./components/Sidebar";
import "./App.css";
import "./Style.css";
import Consult_Question from "./consultant/Consult_Question";
import User_Dash from "./admin/User_Dash";
import Consultant_Dash from "./admin/Consultant_Dash";
import ConsultantProfile from "./consultant-profile/ConsultantProfile";
import OTO_detail from "./OneToOne/OTO_detail";
import OTO_main from "./OneToOne/OTO_main";
import Feedback from "./feedback/Feedback";
import FeedbackDetail from "./feedback/FeedbackDetail";
import FeedbackMain from "./feedback/FeedbackMain";
import FeedbackResume from "./feedback/FeedbackResume";
import ConsultantAdmin from "./admin-consultant/ConsultantAdmin";
import MemberList from "./admin/member/MemberList";
import MemberModal from "./admin/member/MemberModal";
import Login from "./main/Login";
import PrivateRoute from "./components/PrivateRoute"; // PrivateRoute import 추가
import { useSelector } from "react-redux";
import { RootState } from "./store";

const App: React.FC = () => {
  const [isSidebarVisible, setIsSidebarVisible] = useState(true);
  const { token } = useSelector((state: RootState) => state.auth);

  const toggleSidebar = () => {
    setIsSidebarVisible(!isSidebarVisible);
  };

  return (
    <Router>
      <Routes>
        {/* Redirect / to /main/login */}
        <Route
          path="/"
          element={
            token ? (
              <Navigate to="/consultant/consultant-profile" replace />
            ) : (
              <Navigate to="/main/login" replace />
            )
          }
        />

        {/* Login route */}
        <Route
          path="/main/login"
          element={
            token ? <Navigate to="/main" replace /> : <LoginPageLayout />
          }
        />

        {/* Routes that require Header and Sidebar */}
        <Route
          path="/*"
          element={
            token ? (
              <MainLayout
                toggleSidebar={toggleSidebar}
                isSidebarVisible={isSidebarVisible}
              />
            ) : (
              <Navigate to="/main/login" replace />
            )
          }
        />
      </Routes>
    </Router>
  );
};

const MainLayout: React.FC<{
  toggleSidebar: () => void;
  isSidebarVisible: boolean;
}> = ({ toggleSidebar, isSidebarVisible }) => {
  return (
    <div id="app">
      <Header toggleSidebar={toggleSidebar} />
      <i className="bi bi-list fixed-menu-icon" onClick={toggleSidebar}></i>
      <Sidebar isVisible={isSidebarVisible} />
      <main
        className={`main-content ${isSidebarVisible ? "sidebar-open" : ""}`}
      >
        <Routes>
          {/* Pages accessible with rolecd "A" */}
          <Route
            path="/admin/User_Dash"
            element={<PrivateRoute element={<User_Dash />} roleRequired="A" />}
          />
          <Route
            path="/admin/Consultant_Dash"
            element={
              <PrivateRoute element={<Consultant_Dash />} roleRequired="A" />
            }
          />
          <Route
            path="/member/MemberList"
            element={<PrivateRoute element={<MemberList />} roleRequired="A" />}
          />
          <Route
            path="/admin-consultant/ConsultantAdmin"
            element={
              <PrivateRoute element={<ConsultantAdmin />} roleRequired="A" />
            }
          />

          {/* Pages accessible with rolecd "C" */}
          <Route
            path="/consultant/consultant-profile"
            element={
              <PrivateRoute element={<ConsultantProfile />} roleRequired="C" />
            }
          />
          <Route
            path="/consultant/Consult_Question"
            element={
              <PrivateRoute element={<Consult_Question />} roleRequired="C" />
            }
          />
          <Route
            path="/consultant/OneToOne"
            element={<PrivateRoute element={<OTO_main />} roleRequired="C" />}
          />
          <Route
            path="/consultant/OneToOne/detail"
            element={<PrivateRoute element={<OTO_detail />} roleRequired="C" />}
          />
          <Route
            path="/consultant/feedback/list"
            element={<PrivateRoute element={<Feedback />} roleRequired="C" />}
          />
          <Route
            path="/consultant/feedback/detail"
            element={
              <PrivateRoute element={<FeedbackDetail />} roleRequired="C" />
            }
          />
          <Route
            path="/consultant/feedback/main"
            element={
              <PrivateRoute element={<FeedbackMain />} roleRequired="C" />
            }
          />
          <Route
            path="/consultant/feedback/resume"
            element={
              <PrivateRoute element={<FeedbackResume />} roleRequired="C" />
            }
          />
        </Routes>
      </main>
      <i
        className="bi bi-caret-up scroll-to-top"
        onClick={() => window.scrollTo({ top: 0, behavior: "smooth" })}
      >
        <p style={{ display: "inline", fontSize: "20px" }}>TOP</p>
      </i>
      <Footer />
    </div>
  );
};

const LoginPageLayout: React.FC = () => {
  return (
    <div className="login-page">
      <Login />
    </div>
  );
};

export default App;
