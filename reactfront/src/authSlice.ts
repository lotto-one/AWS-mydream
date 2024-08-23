import { createAsyncThunk, createSlice, PayloadAction } from "@reduxjs/toolkit";
import axios from "axios";
import { AppDispatch } from "./store";

interface AuthState {
  token: string | null;
  rolecd: string | null;
  cnsno: string | null;
  error: string | null;
  isAuthenticated: boolean;
}

const initialState: AuthState = {
  token: localStorage.getItem("token") || null,
  rolecd: localStorage.getItem("rolecd") || null,
  cnsno: localStorage.getItem("cnsno") || null,
  error: null,
  isAuthenticated: !!localStorage.getItem("token"),
};

const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    loginSuccess(
      state,
      action: PayloadAction<{
        accessToken: string;
        rolecd: string;
        cnsno: string;
      }>
    ) {
      const { accessToken, rolecd, cnsno } = action.payload;
      state.token = accessToken;
      state.rolecd = rolecd;
      state.cnsno = cnsno;
      state.isAuthenticated = true;
      state.error = null;

      localStorage.setItem("token", accessToken);
      localStorage.setItem("rolecd", rolecd);
      localStorage.setItem("cnsno", cnsno);
    },
    loginFailure(state, action: PayloadAction<string>) {
      state.error = action.payload;
      state.isAuthenticated = false;
    },
    logout(state) {
      state.token = null;
      state.rolecd = null;
      state.cnsno = null;
      state.isAuthenticated = false;

      localStorage.removeItem("token");
      localStorage.removeItem("rolecd");
      localStorage.removeItem("cnsno");
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(login.pending, (state) => {
        state.error = null;
      })
      .addCase(
        login.fulfilled,
        (state, action: PayloadAction<{ rolecd: string }>) => {
          state.rolecd = action.payload.rolecd;
        }
      )
      .addCase(login.rejected, (state, action) => {
        state.error = action.error.message || "Login failed";
      });
  },
});

export const { loginSuccess, loginFailure, logout } = authSlice.actions;

// createAsyncThunk를 사용하여 login 함수 정의
export const login = createAsyncThunk(
  "auth/login",
  async (
    { email, password }: { email: string; password: string },
    { dispatch }
  ) => {
    try {
      const response = await axios.post(
        `${process.env.REACT_APP_BACK_END_URL}/admin/membership/login`,
        {
          email,
          password,
        }
      );

      const { access_token, rolecd, cnsno } = response.data;
      dispatch(loginSuccess({ accessToken: access_token, rolecd, cnsno }));

      return { rolecd, accessToken: access_token, cnsno };
    } catch (error) {
      dispatch(loginFailure("Login failed. Please check your credentials."));
      throw error;
    }
  }
);

export default authSlice.reducer;
