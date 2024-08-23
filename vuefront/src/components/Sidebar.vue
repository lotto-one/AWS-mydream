<template>
  <transition :key="isVisible" name="slide">
    <div class="sidebar" v-if="isVisible">
      <div>
        <div>
          <img
            :src="`http://localhost/mydream/${imgname}`"
            style="
              width: 160px;
              height: 160px;
              radius: 100;
              border-radius: 50%;
              margin-bottom: 20px;
            "
            @error="handleImageError"
          />
          <h3>{{ name }}</h3>
        </div>
      </div>
      <hr />
      <div>
        <router-link to="/Mypage" class="main-router"
          ><p class="bar-p">마이 페이지</p></router-link
        >
        <router-link to="/resumelist" class="main-router"
          ><p class="bar-p">이력서 관리</p></router-link
        >
        <router-link to="ConsultantInfo" class="main-router"
          ><p class="bar-p">컨설턴트 101</p></router-link
        >
        <router-link to="/AISetting" class="main-router"
          ><p class="bar-p">AI면접</p></router-link
        >
        <router-link to="/InterviewRes" class="main-router"
          ><p class="bar-p">면접결과</p></router-link
        >
        <router-link to="/MyConsultantProfile" class="main-router"
          ><p class="bar-p">나의 컨설턴트</p></router-link
        >
        <router-link to="/Calendar" class="main-router"
          ><p class="bar-p">일정관리</p></router-link
        >
        <hr />
        <router-link to="/personalCol_Test" class="main-router"
          ><p class="bar-p">퍼스널컬러</p></router-link
        >
        <div style="align-items: center">
          <i
            class="bi bi-power power-icon"
            style="font-style: normal"
            @click="handleLogout"
            >&nbsp;&nbsp;로그아웃</i
          >
        </div>
      </div>
    </div>
  </transition>
</template>

<script>
import axios from "axios";
import { mapGetters } from "vuex";
import { mapActions } from "vuex";
export default {
  props: {
    isVisible: {
      type: Boolean,
      required: false,
    },
  },
  data() {
    return {
      name: "",
      imgname: "noimage.png", // 기본 이미지 파일명
    };
  },
  mounted() {
    this.imgname = "noimage.png";
    this.loadUserProfile();
  },
  watch: {
    isVisible(newVal) {
      if (newVal) {
        this.imgname = "noimage.png";
        this.loadUserProfile();
      }
    },
  },

  methods: {
    async loadUserProfile() {
      this.imgname = "noimage.png"; // 프로필 로드 전 기본 이미지로 초기화
      const memno = localStorage.getItem("memno");
      if (memno) {
        try {
          const response = await axios.get(
            `${process.env.VUE_APP_BACK_END_URL}/membership/profile?memno=${memno}`
          );
          console.log("response.data=>", response.data);
          this.imgname = response.data.imgname || "noimage.png";
          this.name = response.data.name;
          console.log("this.imgname=> ", this.imgname);
          console.log("this.name=> ", this.name);
          // const { name, imgname } = response.data;
          // this.name = name;
          // // `profileImage`가 있으면 `imgname`을 업데이트, 없으면 기본 이미지 사용
          // this.imgname = imgname || this.imgname;
        } catch (error) {
          console.error("사용자 프로필 로드 오류:", error);
        }
      }
    },
    handleImageError(event) {
      event.target.src = "img/MainPage_image/noimg.png";
    },
    ...mapActions(["logout"]),
    handleLogout() {
      // Call the Vuex logout action
      this.logout()
        .then(() => {
          // Redirect to login page after successful logout
          localStorage.clear();
          this.$router.push("/login");
        })
        .catch((error) => {
          // Handle any errors that occur during logout
          console.error("Logout failed:", error.message);
        });
    },
  },
};
</script>

<style scoped>
.sidebar {
  position: fixed;
  top: 0px; /* 헤더 아래에 위치 */
  left: 0;
  width: 300px;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  padding: 20px;
  padding-top: 60px;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.5);
}

.slide-enter-active,
.slide-leave-active {
  transition: transform 0.3s ease;
}

.slide-enter,
.slide-leave-to {
  transform: translateX(-100%);
}
.bar-p {
  font-size: 18px;
}
.power-icon {
  position: absolute;
  bottom: 60px;
  /* 하단에서 20px 떨어진 위치 */
  left: 31%;
  /* 오른쪽에서 20px 떨어진 위치 */
  font-size: 20px;
  /* 아이콘 크기 조정 */
  cursor: pointer; /* 클릭 가능한 표시 */
}
</style>
