<template>
  <div id="content" class="aisettings-AI-Setting">
    <!-- Progress bar -->
    <div class="aisettings-progress-bar">
      <div class="aisettings-progress" :style="{ width: progress + '%' }"></div>
    </div>

    <!-- Progress steps -->
    <div class="aisettings-progress-text">
      <span :class="{ active: currentStep === 1 }">01. 환경점검</span>
      <span :class="{ active: currentStep === 2 }">02. 사용자 등록</span>
      <span :class="{ active: currentStep === 3 }">03. 지원분야 선택</span>
      <span :class="{ active: currentStep === 4 }">04. 인성면접</span>
      <span :class="{ active: currentStep === 5 }">05. 직무면접</span>
    </div>

    <!-- Interview section -->
    <div class="aisettings-device-check">
      <h3 class="aisettings-AI-interview-title">다음 사항에 유의하세요!</h3>
      <div class="aisettings-notice-section">
        <div class="aisettings-notice-item">
          <div class="aisettings-notice-image">
            <img src="img/notice1.png" alt="얼굴 전체 인식" />
          </div>
          <div class="aisettings-notice-text">
            <h6>
              화면에
              <strong style="color: mediumblue">얼굴 전체가</strong> 잡히게
            </h6>
            <p>
              머리를 좌우, 상하로 흔들지 말고 얼굴 전체가 화면에 잡히도록
              해주세요.
            </p>
          </div>
        </div>

        <div class="aisettings-notice-item">
          <div class="aisettings-notice-image">
            <img src="img/notice2.png" alt="소음 줄이기" />
            <img src="img/notice3.png" alt="목소리 크게" />
          </div>
          <div class="aisettings-notice-text">
            <h6>
              소음은 <strong style="color: mediumblue">줄이고</strong> 목소리는
              <strong style="color: mediumblue">크게</strong>
            </h6>
            <p>
              주변 소음이 심할 경우 음성 인식이 안 돼요! 그리고 목소리는 너무
              작지 않게 해주세요.
            </p>
          </div>
        </div>
      </div>

      <h3 class="aisettings-AI-interview-title">장치 점검</h3>
      <h5>온라인 AI 면접에서는 마우스, 웹캠과 마이크가 필요합니다.</h5>
      <h5>
        각 기기별 버튼을 누르시고
        <strong style="color: mediumblue">장치 상태를 확인</strong>해 주세요.
      </h5>
      <div class="aisettings-device-grid">
        <div
          v-for="device in devices"
          :key="device.name"
          class="aisettings-device-item"
          @click="triggerCheckbox(device)"
        >
          <div class="aisettings-checkbox-container">
            <input
              type="checkbox"
              :id="device.name"
              v-model="device.checked"
              @click.stop
            />
            <label :for="device.name"></label>
          </div>
          <div class="aisettings-device-icon">
            <img :src="device.icon" :alt="device.name" />
          </div>
          <p>{{ device.name }}</p>
        </div>
      </div>

      <!-- Buttons -->
      <div class="aisettings-device-check-btn">
        <button
          class="aisettings-btn aisettings-btn-pre"
          @click="nextToIntChoice"
        >
          점검없이 바로 시작
        </button>
        <transition name="fade" mode="out-in">
          <button
            v-if="allChecked"
            key="next"
            class="aisettings-btn aisettings-btn-next"
            @click="handleNext"
          >
            다음 >
          </button>
        </transition>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      devices: [
        { name: "마우스", icon: "img/mouse.png", checked: false },
        { name: "마이크", icon: "img/mic.png", checked: false },
        { name: "웹캠", icon: "img/camera.png", checked: false },
      ],
      progress: 20,
      currentStep: 1,
    };
  },
  mounted() {
    window.scrollTo(0, 0);

    memno: localStorage.getItem("memno");
  },
  computed: {
    allChecked() {
      return this.devices.every((device) => device.checked);
    },
  },
  methods: {
    handleNext() {
      // AIResumeView로 라우팅
      this.$router.push({ name: "AIFaceTest" });
    },
    nextToIntChoice() {
      this.$router.push({ name: "AIInterviewChoice" });
    },
    triggerCheckbox(device) {
      // console.log(device);
      device.checked = !device.checked;
    },
  },
};
</script>
