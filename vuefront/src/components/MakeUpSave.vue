<template>
  <div class="makeup-overlay">
    <div class="makeup-background">
      <div class="makeup-container">
        <div class="makeup-text">프로필 저장이 완료되었습니다.</div>
        <div class="makeup-btn">
          <button class="makeup-cvlist-btn" @click="$emit('close', false)">
            더 해보기
          </button>
          <button class="makeup-cvhome-btn" @click="home()">홈으로</button>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  props: ["mData"],
  methods: {
    home() {
      this.$router.push("/Main");
      localStorage.removeItem("befimg");
      localStorage.removeItem("uploadedImage");
      localStorage.removeItem("befimgn");
    },
  },
};
</script>
<style scoped>
.makeup-text {
  font-size: 28px;
}
.makeup-btn {
  margin-top: 30px;
}
.makeup-cvlist-btn {
  background-color: #1241a4;
  color: white;
  border: none;
  margin: auto;
  height: 45px;
  width: 100px;
}
.makeup-cvhome-btn {
  background-color: #1241a4;
  color: white;
  border: none;
  margin: auto;
  margin-left: 20px;
  height: 45px;
  width: 100px;
}

.makeup-container {
  border-radius: 10px;
  width: 400px;
  align-content: center;
  background-color: #f9fbff;
  border: 1px solid #c3dbff;
  color: black;
  height: 200px;
  margin-left: 25%;

  opacity: 1; /* 불투명도를 완전히 불투명하게 설정 */
}
.makeup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1500;
  margin: auto;
}
</style>
