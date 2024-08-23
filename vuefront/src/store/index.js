import { createStore } from "vuex";
import axios from "axios";

export default createStore({
  state: {
    //초기화 로컬스토리지 선언
    email: localStorage.getItem("email") || "",
    memno: localStorage.getItem("memno") || "",
    rolecd: localStorage.getItem("rolecd") || "",
    token: localStorage.getItem("token") || "",
    logno: localStorage.getItem("logno") || "", // 추가
  },
  getters: {
    // 인증했는지 판단
    isAuthenticated: (state) => !!state.token,
    email: (state) => state.email,
    rolecd: (state) => state.rolecd,
    accessToken: (state) => state.token, // 'accessToken'을 사용하는 경우
    memno: (state) => state.memno, // 'id' 또는 'memno'를 사용하는 경우
    logno: (state) => state.logno, // 추가
  },
  mutations: {
    setAuth(state, { email, token, memno, rolecd, logno }) {
      state.email = email;
      state.memno = memno;
      state.token = token;
      state.rolecd = rolecd;
      state.logno = logno;
      localStorage.setItem("email", email);
      localStorage.setItem("token", token);
      localStorage.setItem("memno", memno);
      localStorage.setItem("rolecd", rolecd);
      localStorage.setItem("logno", logno); // 추가
    },
    clearAuth(state) {
      state.email = "";
      state.token = "";
      state.memno = "";
      state.rolecd = "";
      state.logno = logno;
      localStorage.removeItem("email");
      localStorage.removeItem("token");
      localStorage.removeItem("memno");
      localStorage.removeItem("rolecd");
      localStorage.removeItem("logno"); // 추가
    },
  },
  actions: {
    login({ commit }, { email, password }) {
      return axios
        .post(`${process.env.VUE_APP_BACK_END_URL}/membership/login`, {
          email,
          password,
        })
        .then((response) => {
          console.log(response.data);

          const { accessToken, memno, rolecd, logno } = response.data; // Postman 결과와 동일하게 맞춤

          commit("setAuth", {
            email,
            token: accessToken,
            memno: memno,
            rolecd: rolecd,
            logno: logno, // 추가
          });
        })
        .catch((error) => {
          console.error(
            "Login error:",
            error.response ? error.response.data : error.message
          );
          return Promise.reject(error);
        });
    },
    logout({ commit, state }) {
      return axios
        .get(`${process.env.VUE_APP_BACK_END_URL}/membership/logout`, {
          params: { logno: state.logno },
        })
        .then(() => {
          commit("clearAuth");
        })
        .catch((error) => {
          console.error(
            "Logout error:",
            error.response ? error.response.data : error.message
          );
        });
    },
    setAuth({ commit }, { email, token, memno, rolecd, logno }) {
      commit("setAuth", { email, token, memno, rolecd, logno });
    },
  },
});
