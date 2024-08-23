<template>
  <div id="app">
    <Header
      v-show="!$route.meta.hideLayout"
      @toggle-sidebar="toggleSidebar"
      :class="{'main-header': true, 'sidebar-open': isSidebarVisible}"
    />
    <i 
      v-show="!$route.meta.hideLayout"
      class="bi bi-list fixed-menu-icon"
      @click="toggleSidebar">
    </i>
    <Sidebar 
      v-show="!$route.meta.hideLayout"
      :isVisible="isSidebarVisible" />
    <main :class="{'main-content': true, 'sidebar-open': isSidebarVisible}">
      <router-view :isSidebarVisible="isSidebarVisible" :setSidebarVisibility="setSidebarVisibility"/>
    </main>
    <i class="bi bi-caret-up scroll-to-top" @click="scrollToTop">
      <p style="display:inline; font-size:20px; font-style: normal;">TOP</p>
    </i>
  </div>
</template>

<script>
import Header from './components/Header.vue'
import Sidebar from './components/Sidebar.vue'

export default {
  components: { Header, Sidebar },
  data() {
    return {
      isSidebarVisible: false, // 사이드바 기본 상태를 닫혀있게 설정
    };
  },
  watch: {
    '$route'(to, from) {
      this.isSidebarVisible = !to.meta.hideLayout; // 라우트 변경 시 사이드바 상태 업데이트
    }
  },
  methods: {
    toggleSidebar() {
      this.isSidebarVisible = !this.isSidebarVisible; // 사이드바 열기/닫기 토글
    },
    setSidebarVisibility(isVisible) {
      this.isSidebarVisible = isVisible; // 사이드바 가시성 설정
    },
    scrollToTop() {
      window.scrollTo({
        top: 0,
        behavior: 'smooth'
      });
    },
  },
}
</script>

<style>
html, body {
  overflow-x: hidden;
}
#app {
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 0;
  min-height: 100vh;
  background: #0c1b49 no-repeat center center fixed;
  background-size: cover;
}

.header-visible, .sidebar-visible, .footer-visible {
  position: relative; /* Ensure lower z-index than .landing */
  z-index: 10;
}

.landing-hidden {
  margin-top: 0; /* Adjust layout if necessary */
}
</style>
