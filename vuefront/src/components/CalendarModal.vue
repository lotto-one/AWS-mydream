<template>
  <div class="modal-overlay" v-if="isModalVisible" @click.self="closeModal">
    <div class="modal1">
      <h2>이벤트 관리</h2>
      <div>
        <label>이벤트 제목</label>
        <input type="text" v-model="localModalEvent.title" />
      </div>
      <div>
        <label>시작 날짜</label>
        <input type="date" v-model="localModalEvent.startdt" />
      </div>
      <div>
        <label>종료 날짜</label>
        <input type="date" v-model="localModalEvent.enddt" />
      </div>
      <div class="modal-buttons">
        <button @click="deleteEvent">삭제</button>
        <button @click="saveEvent">저장</button>
        <button @click="closeModal">닫기</button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, watch, defineComponent } from 'vue';

export default defineComponent({
  props: {
    modalEvent: {
      type: Object,
      required: true,
    },
  },
  emits: ['close', 'update-event', 'delete-event'],
  setup(props, { emit }) {
    const isModalVisible = ref(false);
    const localModalEvent = ref({ ...props.modalEvent });

    watch(
      () => props.modalEvent,
      (newEvent) => {
        if (newEvent) {
          // 날짜를 Date 객체로 변환
          localModalEvent.value = { 
            ...newEvent,
            startdt: newEvent.startdt ? new Date(newEvent.startdt).toISOString().split('T')[0] : '',
            enddt: newEvent.enddt ? new Date(newEvent.enddt).toISOString().split('T')[0] : '',
          };
          isModalVisible.value = true;
        }
      },
      { immediate: true }
    );

    const saveEvent = () => {
      console.log("Saving Event Data:", localModalEvent.value);  // localModalEvent의 값을 출력
      emit('update-event', localModalEvent.value);
    };

    const deleteEvent = () => {
      emit('delete-event', localModalEvent.value);
    };

    const closeModal = () => {
      isModalVisible.value = false;
      emit('close');
    };

    return {
      localModalEvent,
      isModalVisible,
      saveEvent,
      deleteEvent,
      closeModal,
    };
  },
});
</script>
  
  <style scoped>
  .modal-overlay {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 600px; /* 너비 설정 */
    max-width: 90%;
    background: rgba(0, 0, 0, 0.0);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 1200;
  }
  
  .modal1 {
    position: relative;
    background: rgb(255, 255, 255);
    padding: 20px;
    border: 5px solid black;
    width: 100%; /* 모달 크기 조정 */
    max-width: 100%; /* 화면 너비를 최대 100%로 제한 */
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    z-index: 1300;
  }
  
  .modal1 h2 {
    margin-top: 0 ;
  }
  
  .modal1 div {
    margin: 10px ;
  }
  
  .modal1 label {
    display: block ;
    margin-bottom: 5px ;
  }
  
  .modal1 input {
    width: 100% ;
    padding: 8px ;
    box-sizing: border-box ;
    border: 1px solid #ccc !important; /* 강제로 모든 테두리를 설정 */
    border-radius: 4px;
  }
  
  .modal-buttons {
    display: flex;
    justify-content: center;
    width: 100%; /* 버튼 컨테이너의 너비를 줄여서 가운데로 모이게 함 */
    margin: 0 auto; /* 가운데 정렬 */
  }
  
  .modal-buttons button {
    padding: 10px 20px ;
    border: none ;
    background: #007bff ;
    color: white ;
    cursor: pointer ;
    border-radius: 5px ;
  }
  
  .modal-buttons button:hover {
    background: #0056b3 ;
  }
  
  .modal-buttons button:nth-child(1) {
    background: #dc3545 ;
  }
  
  .modal-buttons button:nth-child(1):hover {
    background: #c82333 ;
  }
  
  .modal-buttons button:nth-child(3) {
    background: #6c757d ;
  }
  
  .modal-buttons button:nth-child(3):hover {
    background: #5a6268 ;
  }
  </style>