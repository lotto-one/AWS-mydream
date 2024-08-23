<template>
  <div v-if="visible" class="modal-overlay" @click.self="hide">
    <div class="modal-content99">
      <button class="modal-close99" @click="hide">&times;</button>
      <h5 class="modal-title99">{{ title }}</h5>
      <p class="modal-message99">{{ message }}</p>
      <button class="modal-button99" @click="hide">닫기</button>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    title: {
      type: String,
      default: "모달 제목", // 기본 제목
    },
    message: {
      type: String,
      default: "모달 메시지", // 기본 메시지
    },
  },
  data() {
    return {
      visible: false,
      onHideCallback: null, // 추가된 콜백 함수
    };
  },
  methods: {
    show(callback) {
      this.visible = true;
      this.onHideCallback = callback; // 콜백 함수 저장
    },
    hide() {
      this.visible = false;
      if (this.onHideCallback) {
        this.onHideCallback(); // 콜백 함수 호출
      }
    },
  },
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: -10px;
  left: 10px;
  right: 10px;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content99 {
  background: #fff;
  padding: 20px;
  border-radius: 5px;
  position: relative;
  width: 300px;
}

.modal-close99 {
  position: absolute;
  top: 10px;
  right: 10px;
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
}

.modal-title99 {
  font-size: 25px;
  text-align: center;
  font-weight: 1000;
  margin-bottom: 10px;
}

.modal-message99 {
  text-align: center;
  font-size: 19px;
  margin-top: 20px;
  margin-bottom: 20px;
}

.modal-button99 {
  background-color: #11045e;
  color: #fff;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  margin: 0 auto;
  margin-left: 95px;
}
</style>
