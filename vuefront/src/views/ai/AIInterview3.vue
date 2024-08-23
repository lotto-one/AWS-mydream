<template>
  <div id="content" class="AI-Setting">
    <!-- TTS audio 숨김-->
    <audio ref="audio" style="display: none;"></audio>
    <!-- Progress bar -->
    <div class="progress-bar">
      <div class="progress" :style="{ width: progress + '%' }"></div>
    </div>

    <!-- Progress steps -->
    <div class="progress-text">
      <span :class="{ active: currentStep === 1 }">01. 환경점검</span>
      <span :class="{ active: currentStep === 2 }">02. 사용자 등록</span>
      <span :class="{ active: currentStep === 3 }">03. 지원분야 선택</span>
      <span :class="{ active: currentStep === 4 }">04. 인성면접</span>
      <span :class="{ active: currentStep === 5 }">05. 직무면접</span>
    </div>

    <!-- Interview section -->
    <div class="device-check">
      <h3 class="AI-interview-title">
        질문 3<br /><span v-show="interviewStarted">[인성 질문] {{aiquestion3.totalq}}</span>
      </h3>
      <transition name="fade">
        <div v-if="showInterviewSection">
          <div class="interview-section">
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
                  <video ref="videoElement" autoplay></video>
                </div>
                <div class="ai-face-recognition-overlay"></div>
              </div>    
            </div>

            <!-- Timer section -->
            <div class="timer-section">
              <div class="timer-circle">
                <svg viewBox="0 0 36 36">
                  <path
                    class="circle-bg"
                    d="M18 2.0845
                          a 15.9155 15.9155 0 0 1 0 31.831
                          a 15.9155 15.9155 0 0 1 0 -31.831"
                  />
                  <path
                    class="circle"
                    :stroke-dasharray="progressTime + ', 100'"
                    d="M18 2.0845
                          a 15.9155 15.9155 0 0 1 0 31.831
                          a 15.9155 15.9155 0 0 1 0 -31.831"
                  />
                </svg>
                <div class="timer-text">{{ remainingTime }}<br />SECONDS</div>
              </div>

              <!-- Buttons -->
              <button
                class="btn btn-start-interview"
                v-if="!interviewStarted"
                @click="startInterview"
              >
                면접 시작
              </button>
              <button
                class="btn btn-think-end"
                v-if="isThinkingTime && !isThinkingTimeOver"
                @click="endThinkingTime"
              >
                생각시간 종료
              </button>
              <div
                class="button-group-vertical"
                v-if="isAnsweringTime && remainingTime > 0"
              >
                <button
                  class="btn btn-retry"
                  @click="restartInterview"
                  :disabled="!restartButtonEnabled || hasRestarted"
                >
                  다시 시작하기
                </button>
                <button class="btn btn-submit" @click="submitAnswer">
                  답변 제출
                </button>
              </div>
            </div>
          </div>
        </div>
      </transition>
      <br />

      <!-- Instructions (always visible) -->
      <div class="instruction-section">
        <h4><strong>※ 면접시 주의사항</strong></h4>
        <ul>
          <li><strong style="color: mediumblue">면접 시작 버튼</strong>을 누르면 <strong style="color: mediumblue">생각 시간 30초 타이머</strong>가 활성화됩니다.</li>
          <li>준비가 끝나면 '생각시간 종료' 버튼을 눌러 바로 답변을 시작할 수 있습니다.</li>
          <li><strong style="color: mediumblue">답변 시간은 90초</strong>이며 그 전이라도 답변이 완료되면 답변 제출 버튼을 누르면 됩니다.</li>
          <li>답변이 끝나면 '답변 제출' 버튼을 눌러 조기 종료할 수 있습니다.</li>
          <li><strong style="color: mediumblue">재답변 기회는 답변 시작 후 20초 이후에 <strong style="color: red">1회</strong> 가능합니다.</strong></li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      progress: 72,
      currentStep: 4,
      remainingTime: 30,
      progressTime: 100,
      interval: null,
      isThinkingTime: false,
      isThinkingTimeOver: false,
      isAnsweringTime: false,
      restartButtonEnabled: false,
      allChecked: false,
      hasRestarted: false,
      showInterviewSection: false,
      interviewStarted: false,
      aiquestion3: JSON.parse(localStorage.getItem('questionlist'))[2],
      mediaRecorder: null,
      recordedChunks: [],
      videoStream: null,      
      wehereinpage:true,
    };
  },
  mounted() {    
    this.startStreaming(); // 스트리밍 시작  
    window.scrollTo(0, 0);
    this.convertTextToSpeech();
    this.showInterviewSection = true;          
  },
  methods: {
    async convertTextToSpeech() {
      try {
        const response = await axios.post(
          `${process.env.VUE_APP_DJANGO_APP_BACK_END_URL}/tts/text_to_speech/`,
          { text: this.aiquestion3.totalq },
          { responseType: 'blob' }
        );
        const url = window.URL.createObjectURL(new Blob([response.data], { type: 'audio/mpeg' }));
        this.$refs.audio.src = url;
      } catch (error) {
        console.error('Error converting text to speech:', error);
        alert('Failed to convert text to speech');
      }
    },
    startInterview() {
      this.interviewStarted = true;
      this.startThinkingTime();
      this.$refs.audio.play();
    },
    startThinkingTime() {
      this.isThinkingTime = true;
      this.interval = setInterval(() => {
        if (this.remainingTime > 0) {
          this.remainingTime--;
          this.progressTime = (this.remainingTime / 30) * 100;
        } else {
          clearInterval(this.interval);
          this.endThinkingTime();
        }
      }, 1000);
    },
    endThinkingTime() {
      clearInterval(this.interval);
      this.isThinkingTimeOver = true;
      this.isThinkingTime = false;
      this.isAnsweringTime = true;
      this.remainingTime = 90;
      this.progressTime = 100;
      this.startAnsweringTime();
    },
    startAnsweringTime() {
      this.interval = setInterval(() => {
        if (this.remainingTime > 0) {
          this.remainingTime--;
          this.progressTime = (this.remainingTime / 90) * 100;
          if (this.remainingTime === 70 && !this.hasRestarted) {
            this.restartButtonEnabled = true;
          }
        } else {
          clearInterval(this.interval);
          if(this.wehereinpage){
            this.submitAnswer(); // 녹화가 완료되었는지 여부를 체크하고 전송
          }
          
        }
      }, 1000);

      try{
        // Check and start recording if answering time is active
        if (this.isAnsweringTime && this.mediaRecorder.state === "inactive") {
          this.startRecording(); // 녹화 시작
        }

      }catch{    
        // 너무 빨리 눌렀을 때 1초뒤 레코딩 시작해줌
        setTimeout(() => {
          this.startRecording(); // 녹화 시작
        }, 1000);
      }
    },
    startStreaming() {
      navigator.mediaDevices.getUserMedia({ video: true, audio: true })
        .then(stream => {
          this.videoStream = stream;
          this.$refs.videoElement.srcObject = stream;
          this.isCameraStarted = true;

          // Initialize MediaRecorder
          this.initMediaRecorder();
        })
        .catch(error => {
          console.error('Error starting streaming:', error);
          alert('Failed to start video streaming');
        });
    },
    stopStreaming() {
      if (this.videoStream) {
        this.videoStream.getTracks().forEach(track => track.stop());
        this.videoStream = null;
      }
    },
    initMediaRecorder() {
      if (this.videoStream) {
        this.mediaRecorder = new MediaRecorder(this.videoStream);
        this.mediaRecorder.ondataavailable = (event) => {
          if (event.data.size > 0) {
            this.recordedChunks.push(event.data);
          }
        };
        this.mediaRecorder.onstop = () => {
          console.log('Recording stopped');
        };
      }
    },
    restartInterview() {
      if (!this.hasRestarted) {
        clearInterval(this.interval);
        this.hasRestarted = true;
        this.remainingTime = 90;
        this.progressTime = 100;
        this.restartButtonEnabled = false;
        this.startAnsweringTime();

        // Stop current recording and start a new one
        this.stopRecording();
        
        setTimeout(() => {
          this.recordedChunks = [];
          this.initMediaRecorder();
          this.startRecording();
        }, 500);
      }
    },
    submitAnswer() {
      console.log('제출누름')
      this.stopRecording(); // Stop the current recording
      
      setTimeout(() => {
        this.uploadRecordedVideo(); // Upload the recorded video
      }, 500);
      
    },
    startRecording() {      
      try{
        if (this.mediaRecorder.state === "inactive") {
          this.mediaRecorder.start();
          console.log('Recording started');
        }
      } catch{
        setTimeout(() => {
          this.mediaRecorder.start();
          console.log('Recording started');
        }, 500);
      }       
    },
    stopRecording() {
      if (this.mediaRecorder.state === "recording") {        
          this.mediaRecorder.stop();                
      } else {
        console.log('this.mediaRecorder.state => ',this.mediaRecorder.state)
        this.startRecording();
      }
    },
    uploadRecordedVideo() {
      if (this.recordedChunks.length > 0) {
        const recordedBlob = new Blob(this.recordedChunks, { type: 'video/webm' });

        // Prepare FormData to send the recorded video
        const formData = new FormData();
        formData.append('video', recordedBlob, 'interview.webm');

        // Send the video to the server
        axios.post(
          `${process.env.VUE_APP_DJANGO_APP_BACK_END_URL}/interview/question_detail`,
          formData,
          {
            headers: {
              'Content-Type': 'multipart/form-data',
            },
          }
        )
        .then(response => {
          localStorage.setItem('q3detail', JSON.stringify(response.data));
          
        })
        .catch(error => {
          console.error('Error uploading video:', error);
          alert('Failed to upload video');
        });
        this.$router.push({ name: "AIInterview4" });
      }
    },
  },
  beforeUnmount() {
    this.stopStreaming();
    this.wehereinpage=false;
    
  }
};
</script>


<style scoped>
.progress-bar {
  width: 100%;
  height: 10px;
  background-color: #e0e0e0;
  border-radius: 5px;
  margin-bottom: 10px;
}

.progress {
  height: 100%;
  background-color: #07d0a9;
  border-radius: 5px;
  transition: width 0.5s ease-in-out;
}

.progress-text {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
  padding: 0 10px;
}

.progress-text span {
  flex: 1;
  text-align: center;
  font-size: 1.1rem;
  font-weight: bold;
  color: #6c757d;
  transition: color 0.3s ease;
}

.progress-text span.active {
  color: #ffffff;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s;
}
.fade-enter,
.fade-leave-to {
  opacity: 0;
}

.AI-Setting {
  max-width: 1280px;
  margin: 0 auto;
  padding: 20px;
  background-color: #0c1b49;
  min-height: 100vh;
  width: 100%;
  box-sizing: border-box;
  overflow-y: auto;
}

.AI-interview-title {
  color: #ffffff;
  background-color: #1a2b3c;
  padding: 10px;
  margin-bottom: 20px;
  border: none;
  border-radius: 5px;
  text-align: center;
}

.device-check {
  background-color: #ffffff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  text-align: center;
  min-height: 500px;
  color:black;
}

.interview-section {
  display: flex;
  justify-content: space-evenly;
  align-items: center;
}

.video-preview {
  width: 50%;
}

.video-preview img {
  width: 500px;
}

.timer-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin:80px;
}

.timer-circle {
  width: 150px;
  height: 150px;
  margin-bottom: 20px;
  position: relative;
}

svg {
  transform: rotate(-90deg);
  width: 100%;
  height: 100%;
}

.circle-bg {
  fill: none;
  stroke: #eee;
  stroke-width: 3.8;
}

.circle {
  fill: none;
  stroke: #1659de;
  stroke-width: 2.8;
  stroke-linecap: round;
  transition: stroke-dasharray 1s linear;
}

.timer-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 1.5rem;
  text-align: center;
}

.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: bold;
  font-size: 1.1rem;
  transition: background-color 0.5s, color 0.5s;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.btn-think-end {
  background-color: #07d0a9;
  color: #ffffff;
}

.btn-think-end:hover {
  background-color: #004e4a;
}

.btn-start-interview {
  background-color: #1659de;
  color: #ffffff;
}

.btn-start-interview:hover {
  background-color: #003d8c;
}

.btn-retry {
  background-color: #ffa500;
  color: #ffffff;
  margin-top: 10px;
}

.btn-retry:hover {
  background-color: #ff8c00;
}

.btn-submit {
  background-color: #ff0000;
  color: #ffffff;
}

.btn-submit:hover {
  background-color: #cc0000;
}

.button-group-vertical {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.instruction-section {
  text-align: left;
  background-color: #f8f9fa;
  border-radius: 8px;
  padding: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.instruction-section h4 {
  color: #1659de;
  margin-bottom: 15px;
  font-size: 1.2rem;
}

.instruction-section ul {
  list-style-type: none;
  padding-left: 0;
}

.instruction-section li {
  margin-bottom: 10px;
  position: relative;
  padding-left: 20px;
  line-height: 1.5;
  font-size: 1.1rem;
}

.instruction-section li::before {
  content: "•";
  color: #1659de;
  font-weight: bold;
  position: absolute;
  left: 0;
}

.AI-Setting::-webkit-scrollbar {
  width: 10px;
}

.AI-Setting::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.AI-Setting::-webkit-scrollbar-thumb {
  background: #888;
}

.AI-Setting::-webkit-scrollbar-thumb:hover {
  background: #555;
}

.ai-face-webcam-container {
  margin-right:50px;
}
</style>
