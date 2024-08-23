<!-- MainPage_ConsultantModal.vue -->
<template>
  <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
    <div class="modal-content">
      <button @click="closeModal">Close</button>
      <h2>{{ consultantDetail.consultant.name }} 컨설턴트 상세 정보</h2>
      <!-- 상세 정보 표시 -->
      <p>전문 분야: {{ consultantDetail.consultant.categcd }}</p>
      <!-- 추가 정보 필요시 여기에 추가 -->
    </div>
  </div>
</template>
  
  <script>
export default {
  props: ["consultantDetail", "showModal"],
  methods: {
    closeModal() {
      this.$emit("close");
    },
  },
};
</script>
  
  <style>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  max-width: 500px;
  width: 100%;
}
</style>