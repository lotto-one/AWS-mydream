<template>
  <div v-if="visible" class="modal-overlay">
    <div class="modal-content">
      <button class="modal-close" @click="hide">&times;</button>
      <h5 class="modal-title">비밀번호 변경</h5>
      <form @submit.prevent="submitForm">
        <div class="modal-body">
          <div class="form-group">
            <label for="currentPassword">현재 비밀번호</label>
            <input
              type="password"
              id="currentPassword"
              v-model="currentPassword"
              required
            />
          </div>
          <div class="form-group">
            <label for="newPassword">새 비밀번호</label>
            <input
              type="password"
              id="newPassword"
              v-model="newPassword"
              required
            />
          </div>
          <div class="form-group">
            <label for="confirmPassword">비밀번호 확인</label>
            <input
              type="password"
              id="confirmPassword"
              v-model="confirmPassword"
              required
            />
          </div>
        </div>
        <div class="modal-footer">
          <button type="submit" class="btn-submit">변경</button>
          <button type="button" class="btn-cancel" @click="hide">취소</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      currentPassword: "",
      newPassword: "",
      confirmPassword: "",
    };
  },
  methods: {
    show() {
      this.currentPassword = "";
      this.newPassword = "";
      this.confirmPassword = "";
      this.$emit("update:visible", true);
    },
    hide() {
      this.$emit("update:visible", false);
    },
    submitForm() {
      if (this.newPassword !== this.confirmPassword) {
        alert("새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        return;
      }
      // 부모 컴포넌트로 비밀번호 변경 이벤트 전달
      this.$emit("change-password", {
        currentPassword: this.currentPassword,
        newPassword: this.newPassword,
      });
      this.hide();
    },
  },
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-content {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  width: 400px;
  max-width: 100%;
  position: relative;
}

.modal-close {
  position: absolute;
  top: 10px;
  right: 10px;
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
}

.modal-title {
  font-size: 22px;
  font-weight: 1000;
  margin-bottom: 20px;
}

.modal-body {
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-size: 17px;
}

.form-group input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
}

.btn-submit {
  font-weight: 700;
  background-color: #102669;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
  margin-right: 10px;
}

.btn-cancel {
  font-weight: 700;
  background-color: #6c757d;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
}
</style>
