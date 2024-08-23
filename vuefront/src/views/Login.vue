<template>
  <div class="login-container">
    <!-- 이용 약관 모달이랑 구분하기 위한 div -->
    <div class="cont">
      <div class="lmform sign-in">
        <div class="lmform-area">
          <form method="POST" class="login-form">
            <div class="main-title-center">
              <h2 class="login-title"><b>로그인</b></h2>
            </div>

            <!-- 이메일 -->
            <div class="info-input-container">
              <span class="label-area">
                <label for="email" class="login-label mustInput"
                  >✔️ 이메일</label
                >
              </span>
              <div class="input-area">
                <input
                  class="login-input-text"
                  type="email"
                  id="loginmemail"
                  placeholder="예 ) mydream123@ngg.co.kr"
                  v-model="email"
                  @input="checkEmailFormat"
                />
                <div class="error-msg-area">
                  <!-- <p v-if="emailError" class="login-error-msg">
                    올바른 이메일 형식이 아닙니다.
                  </p> -->
                </div>
              </div>
            </div>
            <br />
            <!-- 비밀번호 -->
            <div class="info-input-container">
              <span class="label-area">
                <label for="password" class="login-label mustInput"
                  >✔️ 비밀번호</label
                >
              </span>
              <div class="input-area">
                <input
                  class="login-input-text"
                  type="password"
                  name="password"
                  id="loginpassword"
                  placeholder=" 대문자, 숫자, 특수문자 포함 8자리"
                  v-model="password"
                  @input="checkPwdFormat"
                  @keyup.enter="submitForm('login')"
                />
              </div>
              <div class="error-msg-area">
                <p v-if="showPwdMsg && !password" class="login-error-msg">
                  비밀번호를 입력해주세요.
                </p>
              </div>
            </div>

            <br />
            <!-- 로그인 폼 -->
            <LoginModal
              ref="loginModal"
              :title="loginTitle"
              :message="loginMessage"
              @close="handleLoginModalClose"
            />
            <div class="login-btn-area">
              <button
                class="login-ctlbtn cancle-btn"
                id="back-btn1"
                type="button"
                @click="back"
              >
                뒤로가기
              </button>
              &nbsp;
              <button
                class="login-ctlbtn select-btn"
                id="login-btn"
                type="button"
                @click="submitForm('login')"
              >
                로그인
              </button>
            </div>
          </form>
        </div>
      </div>
      <div class="sub-cont">
        <div class="img">
          <div class="img__text m--up">
            <h2 class="if-title">처음 오셨다면?</h2>
            <p>회원가입하시고 당신의 꿈을 향해 한발 내딛어보세요!</p>
          </div>
          <div class="img__text m--in">
            <h2 class="if-title">이미 계정이 있다면,</h2>
            <p>로그인하고 당신의 꿈을 가꾸세요!</p>
          </div>
          <div class="img__btn">
            <span class="m--up">Sign Up</span>
            <span class="m--in">Sign In</span>
          </div>
        </div>
        <div class="lmform sign-up">
          <form
            method="POST"
            autocomplete="off"
            @submit.prevent="submit"
            class="join-form"
          >
            <div class="main-title-center">
              <h2 class="join-title">
                <b>회원가입</b>
              </h2>
            </div>

            <!-- 이메일 -->
            <div class="info-input-container">
              <span class="label-area">
                <label for="email" class="join-label mustInput"
                  >✔️ 이메일 &nbsp;&nbsp;</label
                >
              </span>
              <div class="input-area">
                <input
                  class="join-input-text"
                  type="text"
                  v-model="email"
                  @input="checkEmailFormat"
                  id="email"
                  placeholder="이메일 입력"
                />
                <button
                  class="email-check-btn"
                  id="email-ckeck"
                  @click="emailCheck"
                >
                  인증
                </button>

                <!-- SignupModal 컴포넌트 사용 -->
                <SignupModal
                  ref="signupModal"
                  :title="modalTitle"
                  :message="modalMessage"
                  @close="handleModalClose"
                />

                <div class="error-msg-area">
                  <!-- <p v-if="emailError" class="login-error-msg">
                    올바른 이메일 형식이 아닙니다.
                  </p> -->
                  <p style="display: none" id="emailCheck-msg" class="msg"></p>
                </div>
              </div>
            </div>

            <!-- 이메일 인증 번호 -->
            <div class="info-input-container">
              <span class="label-area">
                <label for="code" class="join-label mustInput"
                  >✔️ 인증번호</label
                >
              </span>
              <div class="input-area">
                <input
                  class="join-input-text"
                  type="text"
                  @keydown.enter.prevent="handleEnter"
                  id="code"
                  :vmodel="code"
                  placeholder="인증번호 입력"
                />
                <button
                  class="email-check-btn"
                  id="email-ckeck"
                  @click="verifyCode"
                >
                  인증확인
                </button>
              </div>
              <div class="error-msg-area">
                <p style="display: none" id="Code-msg" class="msg"></p>
              </div>
            </div>

            <!-- 모달 -->
            <div
              class="modal fade"
              id="errorModal"
              tabindex="-1"
              role="dialog"
              aria-labelledby="errorModalLabel"
              aria-hidden="true"
            >
              <div class="modal-dialog" role="document">
                <div class="modal-content">
                  <div class="modal-header">
                    <h5 class="modal-title" id="errorModalLabel">오류</h5>
                    <button
                      type="button"
                      class="close"
                      data-dismiss="modal"
                      aria-label="Close"
                    >
                      <span aria-hidden="true">&times;</span>
                    </button>
                  </div>
                  <div class="modal-body">인증번호가 올바르지 않습니다.</div>
                  <div class="modal-footer">
                    <button
                      type="button"
                      class="btn btn-secondary"
                      data-dismiss="modal"
                    >
                      닫기
                    </button>
                  </div>
                </div>
              </div>
            </div>

            <!-- 비밀번호 -->
            <div class="info-input-container">
              <span class="label-area">
                <label for="password" class="join-label mustInput"
                  >✔️ 비밀번호</label
                >
              </span>
              <div class="input-area">
                <input
                  class="join-input-text"
                  type="password"
                  name="password"
                  id="password"
                  placeholder="대문자, 숫자, 특수문자 포함 8자리"
                  v-model="password"
                  @input="checkPwdFormat"
                />

                <div class="error-msg-area">
                  <p v-if="pwdError" class="login-error-msg">
                    올바른 비밀번호 형식이 아닙니다.
                  </p>
                </div>
              </div>
            </div>

            <!-- 이름 -->
            <div class="info-input-container">
              <span class="label-area">
                <label for="name" class="join-label mustInput">✔️ 이름</label>
              </span>
              <div class="input-area">
                <input
                  class="join-input-text"
                  type="text"
                  name="name"
                  id="name"
                  v-model="name"
                  placeholder="이름 입력"
                />
              </div>
              <div class="error-msg-area">
                <p style="display: none" id="nameCheck-msg" class="msg"></p>
              </div>
            </div>

            <!-- 이름 -->
            <div class="info-input-container">
              <span class="label-area">
                <label for="nickname" class="join-label mustInput"
                  >✔️ 닉네임</label
                >
              </span>
              <div class="input-area">
                <input
                  class="join-input-text"
                  type="text"
                  name="nickname"
                  id="nickname"
                  v-model="nickname"
                  placeholder="닉네임 입력"
                />
              </div>
            </div>
            <br />

            <!-- 생년월일 -->
            <div class="info-input-container">
              <span class="label-area">
                <label for="birthymd" class="join-label dateCHeck"
                  >✔️ 생년월일</label
                >
              </span>
              <div class="input-area">
                <Datepicker
                  locale="ko"
                  v-model="birthymd"
                  class="datepicker"
                  :enable-time-picker="false"
                  style="display: inline-block"
                  format="yyyy년 MM월 dd일"
                />
              </div>
            </div>
            <br />

            <!-- 전화번호 -->
            <div class="info-input-container">
              <span class="label-area">
                <label for="phonenum" class="join-label mustInput"
                  >✔️ 전화번호</label
                >
              </span>
              <div class="input-area">
                <input
                  class="join-input-text"
                  type="tel"
                  name="phonenum"
                  id="phonenum"
                  v-model="phonenum"
                  @input="formatPhoneNumber"
                  placeholder="전화번호 입력"
                />
              </div>
            </div>
            <br />
            <!-- 성별 -->
            <div class="info-input-container">
              <span class="label-area">
                <label for="gendercd" class="join-label chooseRadio"
                  >✔️ 성별</label
                >
              </span>
              <div class="gender-input-area">
                <input
                  type="radio"
                  v-model="gendercd"
                  value="M"
                  name="gendercd"
                />
                <label
                  for="option1"
                  class="join-label custom-radio"
                  style="padding-left: 0px"
                ></label
                >남성
                <input
                  type="radio"
                  v-model="gendercd"
                  value="F"
                  name="gendercd"
                />
                <label
                  for="option1"
                  class="join-label custom-radio"
                  style="padding-left: 0px"
                ></label
                >여성
              </div>
            </div>
            <br />
            <!-- 희망직무 -->
            <div class="info-input-container">
              <span class="label-area">
                <label for="categcd" class="join-label chooseRadio"
                  >✔️ 희망직무</label
                >
              </span>
              <div class="job-input-area">
                <input
                  type="radio"
                  v-model="categcd"
                  name="categcd"
                  value="1"
                  class="job"
                />IT/개발
                <input
                  type="radio"
                  v-model="categcd"
                  name="categcd"
                  value="2"
                  class="job"
                />교육
                <input
                  type="radio"
                  v-model="categcd"
                  name="categcd"
                  value="3"
                  class="job"
                />영업/마케팅
                <input
                  type="radio"
                  v-model="categcd"
                  name="categcd"
                  value="4"
                  class="job"
                />기획/전략
                <input
                  type="radio"
                  v-model="categcd"
                  name="categcd"
                  value="5"
                  class="job"
                />경영
              </div>
            </div>
            <br />

            <!-- 희망직무 -->
            <div class="info-input-container">
              <span class="label-area">
                <label for="loccd" class="join-label chooseRadio"
                  >✔️ 희망직무</label
                >
              </span>
              <div class="job-input-area">
                <input
                  type="radio"
                  v-model="loccd"
                  name="loccd"
                  value="1"
                  class="job"
                />서울
                <input
                  type="radio"
                  v-model="loccd"
                  name="loccd"
                  value="2"
                  class="job"
                />경기도
                <input
                  type="radio"
                  v-model="loccd"
                  name="loccd"
                  value="3"
                  class="job"
                />충청도
                <input
                  type="radio"
                  v-model="loccd"
                  name="loccd"
                  value="4"
                  class="job"
                />전라도
                <input
                  type="radio"
                  v-model="loccd"
                  name="loccd"
                  value="5"
                  class="job"
                />경상도

                <input
                  type="radio"
                  v-model="loccd"
                  name="loccd"
                  value="6"
                  class="job"
                />강원도
                <input
                  type="radio"
                  v-model="loccd"
                  name="loccd"
                  value="7"
                  class="job"
                />제주도
              </div>
            </div>
            <br />

            <!-- 버튼 -->
            <div class="myinfo-btn-area">
              <button class="big-ctlbtn cancle-btn" id="back-btn" @click="back">
                뒤로가기
              </button>
              &nbsp;
              <button
                class="big-ctlbtn select-btn"
                id="join-btn"
                type="button"
                @click="submitForm('signup')"
                :disabled="!canSubmit"
                :class="{
                  'btn-disabled': !canSubmit,
                  'btn-active': canSubmit,
                }"
              >
                가입하기
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import SignupModal from "../components/SignupModal.vue";
import LoginModal from "../components/LoginModal.vue";
import { mapActions } from "vuex";
import axios from "axios";
import { ref, onMounted, defineComponent } from "vue";
import Datepicker from "@vuepic/vue-datepicker";
import "@vuepic/vue-datepicker/dist/main.css";

export default defineComponent({
  components: {
    Datepicker,
    SignupModal,
    LoginModal,
  },
  setup() {
    const email = ref("");
    const name = ref("");
    const birthymd = ref(null); // v-model에 사용할 변수

    const rightPanelActive = ref(false);

    const code = ref("");
    const verificationMessage = ref(""); // 인증 결과 메시지를 저장할 속성
    const toggleSignup = () => {
      const cont = document.querySelector(".cont");
      cont.classList.toggle("s--signup");
    };
    const formatBirthDate = (date) => {
      //생일변환했음
      if (!date) return "";
      const d = new Date(date);
      const year = d.getFullYear();
      const month = String(d.getMonth() + 1).padStart(2, "0"); // Ensure two digits
      const day = String(d.getDate()).padStart(2, "0"); // Ensure two digits
      return `${year}${month}${day}`; // Format to YYYYMMDD
    };

    // 이벤트 리스너 추가
    onMounted(() => {
      const imgBtn = document.querySelector(".img__btn");
      if (imgBtn) {
        imgBtn.addEventListener("click", toggleSignup);
      }
    });

    return {
      email,
      name,
      birthymd,
      rightPanelActive,
      code,
      verificationMessage,
      formatBirthDate,
    };
  },
  data() {
    return {
      isEmailVerified: false,
      email: "",
      code: "",
      name: "",
      gendercd: "",
      nickname: "",
      categcd: "",
      password: "",
      phonenum: "",
      loccd: "",
      emailError: false,
      showPwdMsg: false,
      pwdError: false,
      modalTitle: "", // 모달 제목
      modalMessage: "", // 모달 메시지
      loginTitle: "",
      loginMessage: "",
      registrationSuccess: false, // 회원가입 성공 여부를 기록합니다.
    };
  },

  methods: {
    formatPhoneNumber(event) {
      // 사용자가 입력한 값을 가져옵니다.
      let input = event.target.value;

      // 하이픈을 제거하고 숫자만 남깁니다.
      input = input.replace(/\D/g, "");

      // 3-4-4 포맷으로 하이픈 추가
      if (input.length <= 3) {
        input = input;
      } else if (input.length <= 7) {
        input = `${input.slice(0, 3)}-${input.slice(3)}`;
      } else {
        input = `${input.slice(0, 3)}-${input.slice(3, 7)}-${input.slice(7, 11)}`;
      }

      // 모델을 업데이트하여 하이픈이 포함된 값을 보여줍니다.
      this.phonenum = input;
    },
    back() {
      // 뒤로가기 기능 구현
      window.history.back();
    },
    toggleRightPanel(active) {
      this.rightPanelActive = active;
    },
    checkEmailFormat() {
      // 이메일 형식 검증 메서드
      this.emailError = !this.validateEmail(this.email);
    },
    validateEmail(email) {
      // 간단한 이메일 형식 검증 로직
      return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
    },
    emailCheck() {
      const email = document.getElementById("email").value;
      axios
        .post(`${process.env.VUE_APP_BACK_END_URL}/api/auth/emailCheck`, {
          email: email,
        })
        .then((res) => {
          // alert("Res DAta:" + res.data);
          if (res.data === 0) {
            // alert("인증 번호가 발송되었습니다.");
            this.modalTitle = "이메일 인증 성공"; // 모달 제목 설정
            this.modalMessage = "인증 번호가 발송되었습니다."; // 모달 메시지 설정
            this.$refs.signupModal.show(); // 모달을 표시
            // document.getElementById("emailCheck-msg").style.display = "none";
          } else {
            this.modalTitle = "이메일 인증 실패"; // 모달 제목 설정
            this.modalMessage = "이미 사용중인 이메일입니다."; // 모달 메시지 설정
            this.$refs.signupModal.show(); // 모달을 표시
            // document.getElementById("emailCheck-msg").innerHTML =
            //   "이미 사용중인 이메일입니다.";
            // document.getElementById("emailCheck-msg").style.display = "block";
          }
        })
        .catch((error) => {
          // alert("인증 번호 발송에 오류가 발생했습니다.");
          this.modalTitle = "이메일 인증 실패"; // 모달 제목 설정
          this.modalMessage = "올바르지않은 이메일 형식입니다."; // 모달 메시지 설정
          this.$refs.signupModal.show();
          console.error("API 호출 에러(인증번호 발송)", error);
          return false;
        });
    },
    verifyCode() {
      const email = document.getElementById("email").value;
      const code = document.getElementById("code").value;
      axios
        .post(
          `${process.env.VUE_APP_BACK_END_URL}/api/auth/emailCheck/certification`,
          {
            email: email,
            code: code,
          }
        )
        .then((res) => {
          if (res.data) {
            // alert("인증번호가 확인되었습니다.");
            this.modalTitle = "인증 성공";
            this.modalMessage = "인증번호가 확인되었습니다.";
            this.$refs.signupModal.show();
            this.isEmailVerified = true;
          } else {
            // alert("인증번호가 올바르지 않습니다.");
            this.modalTitle = "인증 실패";
            this.modalMessage = "인증번호가 올바르지 않습니다.";
            this.$refs.signupModal.show();
            this.isEmailVerified = false;
          }
        })
        .catch((error) => {
          // alert("인증번호 확인에 오류가 발생했습니다.");
          this.modalTitle = "오류";
          this.modalMessage = "인증 중 오류가 발생했습니다.";
          this.$refs.signupModal.show();
          console.error("API 호출 에러(인증번호 확인)", error);
          this.isEmailVerified = false;
        });
    },
    checkPwdFormat() {
      this.pwdError = !this.validatePwd(this.password);
    },
    validatePwd(pwd) {
      // 간단한 비밀번호 형식 검증 로직 (대문자, 숫자, 특수문자 포함 8자리 이상)
      return /^(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,}$/.test(
        pwd
      );
    },
    ...mapActions(["login"]),

    submitForm(formType) {
      if (formType === "login") {
        // 로그인 폼 제출
        this.checkEmailFormat();
        this.checkPwdFormat();

        if (!this.emailError) {
          if (!this.password) {
            this.showPwdMsg = true;
            return;
          } else {
            this.showPwdMsg = false;
          }
          console.log(this.email);
          console.log(this.password);
          this.login({ email: this.email, password: this.password })
            .then(() => {
              this.loginTitle = "";
              this.loginMessage = "로그인 성공";
              this.$refs.loginModal.show(this.redirectToMain);
            })
            .catch((err) => {
              console.log(err.message);
              if (err.response && err.response.status === 401) {
                this.loginTitle = "";
                this.loginMessage = "비밀번호가 잘못되었습니다.";
                this.$refs.loginModal.show(); // 로그인 폼에서 호출
              } else {
                alert("로그인에 실패했습니다.");
              }
            });
        } else {
          this.loginTitle = "";
          this.loginMessage = "등록되지않은 이메일 입니다.";
          this.$refs.loginModal.show(); // 로그인 폼에서 호출
        }
      } else if (formType === "signup") {
        // 회원가입 폼 제출
        this.checkEmailFormat();
        this.checkPwdFormat();

        if (this.emailError || this.pwdError) {
          alert("입력된 정보에 오류가 있습니다.");
          return;
        }

        const formattedBirthymd = this.formatBirthDate(this.birthymd);
        const signupData = {
          email: this.email,
          name: this.name,
          password: this.password,
          birthymd: formattedBirthymd,
          gendercd: this.gendercd,
          nickname: this.nickname,
          categcd: this.categcd,
          phonenum: this.phonenum,
          loccd: this.loccd,
        };

        console.log("Signup data:", signupData); // 콘솔에 출력
        // 회원가입 요청
        axios
          .post(
            `${process.env.VUE_APP_BACK_END_URL}/membership/register`,
            signupData
          )
          .then((res) => {
            console.log("서버응답:", res.data);
            if (res.data === "User registered successfully") {
              this.registrationSuccess = true;
              this.modalTitle = "";
              this.modalMessage = "회원가입이 완료되었습니다.";
              this.$refs.signupModal.show(this.redirectToLogin); // 콜백 함수 전달
              // alert("회원가입이 완료되었습니다.");
              // window.location.href = "/login";
            } else {
              this.modalTitle = "";
              this.modalMessage = "회원가입에 실패하였습니다.";
              this.$refs.signupModal.show();
              // alert("회원가입에 실패하였습니다.");
            }
          })
          .catch((error) => {
            alert("회원가입 중 오류가 발생했습니다.");
            console.error("API 호출 에러(회원가입)", error);
          });
      }
    },

    redirectToLogin() {
      window.location.href = "/login";
    },
    redirectToMain() {
      window.location.href = "/main";
    },
  },
  computed: {
    canSubmit() {
      // 모든 유효성 검사가 통과되고 이메일 인증이 완료된 경우 true 반환
      return (
        !this.emailError &&
        !this.pwdError &&
        this.isEmailVerified &&
        this.email.trim() !== "" &&
        this.password.trim() !== "" &&
        this.name.trim() !== "" &&
        this.nickname.trim() !== "" &&
        this.phonenum.trim() !== "" &&
        this.gendercd.trim() !== "" &&
        this.categcd.trim() !== "" &&
        this.loccd.trim() !== "" &&
        this.birthymd // 생년월일은 빈값 체크를 다르게 할 수 있습니다.
      );
    },
  },
});
</script>

<style>
.btn-disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.btn-active {
  opacity: 1;
  cursor: pointer;
}

.join-label custom-radio {
  padding-left: 500px;
}

.dp__pointer {
  text-align: center;
}
</style>
