<template>
  <div class="header" :class="{ hidden: isHidden }">
    <h1 class="header-h1">
      <router-link to="/main" style="color: white; text-decoration: none"
        >내가, 꿈</router-link
      >
      <div>
        <router-link to="/login" class="custom-underline">
          <h5
            class="header-h5"
            style="padding-right: 20px"
            @click="handleLogout"
          >
            로그아웃
          </h5>
        </router-link>
      </div>
    </h1>
  </div>
</template>
<script>
import { mapActions } from "vuex";
export default {
  data() {
    return {
      isHidden: false,
      lastScrollY: 0,
    };
  },
  methods: {
    toggleSidebar() {
      this.$emit("toggle-sidebar");
    },
    handleScroll() {
      this.isHidden = window.scrollY !== 0;
    },
    // Map the logout action from the Vuex store
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
  mounted() {
    window.addEventListener("scroll", this.handleScroll);
  },
  beforeDestroy() {
    window.removeEventListener("scroll", this.handleScroll);
  },
};
</script>
<style scoped>
.header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  background-color: rgba(255, 255, 255, 0); /* 반투명 배경색 */
  color: #333;
  margin: auto;
  height: 60px;
  z-index: 1000;
}
.header.hidden {
  transform: translateY(-100%); /* 헤더 숨기기 */
}
.header-h1 {
  display: flex;
  align-items: center; /* 수직 정렬 */
  justify-content: space-between; /* 요소 사이에 공간 분배 */
  text-align: left;
  font-size: 30px;
  color: white;
  margin: auto;
  padding: 20px 50px 15px 60px;
  height: 60px;
}
.header-h5 {
  display: inline;
}
.custom-underline {
  color: white;
  text-decoration: none; /* 기본 언더라인 제거 */
  position: relative;
}

.custom-underline::after {
  content: "";
  display: block;
  position: absolute;
  bottom: 0px; /* 조정 필요 */
  left: 0;
  width: 75px;
  height: 1px; /* 언더라인 두께 조절 */
  background: white; /* 언더라인 색상 */
}
</style>
