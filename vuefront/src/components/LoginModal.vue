<template>
  <div v-if="visible" class="login-modal-overlay" @click.self="hide">
    <div class="login-modal-content">
      <button class="login-modal-close" @click="hide">&times;</button>
      <h5 class="login-modal-title">{{ title }}</h5>
      <p class="login-modal-message">{{ message }}</p>
      <button class="login-modal-button" @click="hide">닫기</button>
    </div>
  </div>
</template>

<script>
export default {
  name: "LoginModal",
  props: {
    title: {
      type: String,
      default: "로그인 모달 제목",
    },
    message: {
      type: String,
      default: "로그인 모달 메시지",
    },
  },
  data() {
    return {
      visible: false,
      onHideCallback: null,
    };
  },
  methods: {
    show(callback) {
      this.visible = true;
      this.onHideCallback = callback;
    },
    hide() {
      this.visible = false;
      if (this.onHideCallback) {
        this.onHideCallback();
      }
    },
  },
};
</script>

<style scoped>
.login-modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-modal-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 300px;
  position: relative;
}

.login-modal-close {
  position: absolute;
  top: 10px;
  right: 10px;
  border: none;
  background: transparent;
  font-size: 20px;
}

.login-modal-title {
  font-size: 18px;
  margin-bottom: 10px;
}

.login-modal-message {
  font-size: 19px;
  margin-bottom: 20px;
}

.login-modal-button {
  padding: 10px 20px;
  background-color: #102669;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
</style>
