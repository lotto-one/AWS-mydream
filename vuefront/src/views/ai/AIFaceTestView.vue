<template>
  <div id="content" class="ai-face-AI-Setting">
    <!-- Progress bar -->
    <div class="ai-face-progress-bar">
      <div class="ai-face-progress" :style="{ width: progress + '%' }"></div>
    </div>

    <!-- Progress steps -->
    <div class="ai-face-progress-text">
      <span :class="{ active: currentStep === 1 }">01. 환경점검</span>
      <span :class="{ active: currentStep === 2 }">02. 사용자 등록</span>
      <span :class="{ active: currentStep === 3 }">03. 지원분야 선택</span>
      <span :class="{ active: currentStep === 4 }">04. 인성면접</span>
      <span :class="{ active: currentStep === 5 }">05. 직무면접</span>
    </div>

    <!-- Interview section -->
    <div class="ai-face-device-check">
      <h3 class="ai-face-AI-interview-title">얼굴 인식을 시작합니다</h3>
      <h5>응시자의 얼굴을 인식하는 단계입니다.</h5>
      <h5>
        <strong style="color: mediumblue">얼굴을 네모 박스 안에 위치</strong>시키고 아래
        <strong style="color: mediumblue">얼굴 인식 버튼을 누르고 </strong>조금
        기다려 주세요.
      </h5>

      <!-- Webcam section -->
      <div class="ai-face-webcam-container">
        <div
          class="ai-face-webcam-frame"
          :class="{
            'ai-face-recognition-active': isRecognizing,
            'ai-face-recognition-complete': isRecognitionComplete,
          }"
        >
        <div class="ai-face-image-container">
          <img
              src="img/startimg.png"
              class="ai-face-webcam-image"
              :class="{ 'ai-face-fade-out': isRecognizing }"
              v-show="!isCameraStarted"
            />
              <video ref="videoElement" autoplay></video>
        </div>
        <div class="ai-face-face-recognition-box" v-show="isCameraStarted"></div>
        <div class="ai-face-recognition-overlay"></div>
          
            <!-- Start Camera 버튼 -->
            <button 
              class="ai-face-btn ai-face-btn-recognition"
              @click="startCamera" 
              v-show="!isCameraStarted">
              Start
            </button>

            <!-- 얼굴 인식 버튼 -->
            <button
              class="ai-face-btn ai-face-btn-recognition"
              :class="{ 'btn-success': isRecognitionComplete }"
              @click="startRecognition"
              :disabled="isRecognitionComplete"
              v-show="isCameraStarted"
            >
                    {{ recognitionButtonText }}
            </button>
          
        </div>    
      </div>
      
      <div class="ai-face-device-check-btn">
        <button class="ai-face-btn ai-face-btn-pre" @click="handleBack">< 이전</button>
        <transition name="fade" mode="out-in">
          <button
            v-if="isRecognitionComplete"
            key="next"
            class="ai-face-btn ai-face-btn-next"
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
import axios from 'axios';
export default {
  data() {
    return {
      progress: 30,
      currentStep: 2,
      isRecognitionComplete: false,
      videoStream: null,
      recognitionButtonText: "얼굴 인식 시작",
      isCameraStarted: false,
    };
  },
  methods: {
    handleBack() {
      this.$router.push({ name: "AISetting" });
    },
    handleNext() {
      this.$router.push({ name: "AIMicTest" });
    },
    startCamera() {
      navigator.mediaDevices.getUserMedia({ video: true })
        .then((stream) => {
          this.videoStream = stream;
          this.$refs.videoElement.srcObject = stream;
          this.isCameraStarted = true;
        })
        .catch((error) => {
          console.error('Error accessing webcam:', error);
        });
    },
    stopCamera() {
      if (this.videoStream) {
        this.videoStream.getTracks().forEach(track => track.stop());
        this.videoStream = null;
      }
    },
    captureImage() {
      const video = this.$refs.videoElement;
      const canvas = document.createElement('canvas');
      canvas.width = video.videoWidth;
      canvas.height = video.videoHeight;
      const context = canvas.getContext('2d');
      context.drawImage(video, 0, 0, canvas.width, canvas.height);
      return canvas.toDataURL('image/jpeg');
    },
    async sendImageToServer(imageData) {
      try {
        console.log('axios출발')
        const response = await axios.post(`${process.env.VUE_APP_DJANGO_APP_BACK_END_URL}/interview/face_detect`, {
          image: imageData
        }, {
          headers: {
            'Content-Type': 'application/json'
          }
        });      
        
        if (response.data.result=='Face detected!') {
          console.log('Server response:', response.data);
          this.recognitionButtonText = "얼굴 인식 성공";
          this.isRecognitionComplete = true;
        } else{
          alert('얼굴을 인식시켜주세요!')
        }
        
      } catch (error) {
        console.error('Error uploading image:', error);
      }
    },
    startRecognition() {
      console.log('버튼 눌림');
      const imageData = this.captureImage();
      this.sendImageToServer(imageData);
    }
  },
  beforeUnmount() {
    this.stopCamera();
  }
};

</script>

<style scoped>
video {
  width: 100%;
  height: auto;
  border: 1px solid #ccc; /* optional: add a border to better visualize the video */
}
</style>
