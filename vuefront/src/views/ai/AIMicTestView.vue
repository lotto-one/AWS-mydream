<template>
  <div id="content" class="AI-Setting">
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
      <h3 class="AI-interview-title">음성 인식을 시작합니다</h3>
      <h3 style="font-weight: bold; margin-bottom: 10px">
        응시자의 음성을 인식하는 단계입니다.
      </h3>
      <h5>
        <p style="margin-bottom: 6px">
          <strong style="color: mediumblue">아래 녹음하기 버튼</strong>을 누른
          뒤,
          <strong style="color: mediumblue"
            >"만나서 반가워요. 저는 지원자 {{ memname }}입니다"</strong
          >라고 말씀해 주세요.
        </p>
        <strong style="color: red">녹음 중지 버튼</strong>을 누르면,
        <strong style="color: mediumblue">인식된 음성이 텍스트로</strong>
        나타납니다.
      </h5>

      <div class="canvas-container">
        <div class="canvas-frame">
          <canvas
            class="audio-canvas"
            ref="visualizerCanvas"
            width="640px"
            height="480px"
          ></canvas>

          <button
            v-if="isRecording == 1"
            class="btn btn-recognition"
            @click="startRecording"
          >
            녹음하기
          </button>
          <button
            v-if="isRecording == 2"
            class="btn btn-recognition btn-recording"
            @click="stopRecording"
          >
            녹음 중지
          </button>

          <div v-if="isRecording == 3" class="re-or-done">
            <button class="btn btn-retry" @click="startRecording">
              다시하기
            </button>
            <button class="btn btn-success" @click="doneRecord">
              완료하기
            </button>
          </div>
          <h5 class="stt-result">{{ sttResult }}</h5>
        </div>
      </div>

      <!-- Webcam section 
      <div class="webcam-container">
        <div class="webcam-frame">
          <video ref="video" autoplay muted></video>
          <div class="recognition-status">
            {{ recognitionStatus }}
          </div>
          <div class="image-container" v-if="isRecording">
            <img src="img/mictest.gif" class="mic-test-gif" />
          </div>

          Buttons
          <button
            class="btn btn-recognition"
            :class="{
              'btn-recording': isRecording,
              'btn-success': isRecognitionComplete,
              'btn-retry':
                !isRecording && !isRecognitionComplete && hasAttempted,
            }"
            @click="toggleRecording"
            :disabled="isRecognitionComplete"
          >
            {{ buttonText }}
          </button>
        </div>
      </div>-->

      <div class="device-check-btn">
        <button class="btn btn-pre" @click="handleBack">< 이전</button>
        <transition name="fade" mode="out-in">
          <button
            v-if="isRecognitionComplete"
            key="next"
            class="btn btn-next"
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
import axios from "axios";
export default {
  data() {
    return {
      memname: "",
      progress: 40,
      currentStep: 2,
      isRecognizing: false,
      isRecognitionComplete: false,
      isRecording: 1,
      recognitionStatus: "",
      buttonText: "녹음하기",
      hasAttempted: false,
      memno: localStorage.getItem("memno"),
      audioContext: null,
      analyser: null,
      dataArray: null,
      canvasContext: null,
      bufferLength: 0,
      isVisualizing: false, // 시각화 상태를 추적
      stream: null,
      recordedChunks: [],
      sttResult: "",
      mediaRecorder: null,
    };
  },
  mounted() {
    window.scrollTo(0, 0);
    this.memno = localStorage.getItem("memno");
    axios
      .get(
        `${process.env.VUE_APP_BACK_END_URL}/interview/getname?memno=${this.memno}`
      )
      .then((res) => {
        this.memname = res.data;
      })
      .catch((error) => {
        console.error("에러발생 에러발생");
      });
  },
  methods: {
    handleBack() {
      this.$router.push({ name: "AIFaceTest" });
    },
    handleNext() {
      this.$router.push({ name: "AIInterviewChoice" });
    },
    async startRecording() {
      if (this.isVisualizing) return; // Skip if already visualizing
      this.sttResult = "";
      this.isRecognitionComplete = false;

      this.isRecording = 2;
      this.recordedChunks = []; // Reset recorded chunks

      try {
        const stream = await navigator.mediaDevices.getUserMedia({
          audio: true,
        });
        this.stream = stream; // Save the stream for stopping later

        // Start media recorder
        this.mediaRecorder = new MediaRecorder(stream,{
          mimeType: "audio/webm"  // 또는 "audio/wav", "audio/ogg" 등 지원되는 형식
        });
        this.mediaRecorder.ondataavailable = (event) => {
          if (event.data.size > 0) {
            this.recordedChunks.push(event.data);
          }
        };
        this.mediaRecorder.onstop = this.handleStop;
        this.mediaRecorder.start();

        // Initialize audio context and analyser if needed
        this.audioContext = new (window.AudioContext ||
          window.webkitAudioContext)();
        this.analyser = this.audioContext.createAnalyser();
        const source = this.audioContext.createMediaStreamSource(stream);
        source.connect(this.analyser);
        this.analyser.fftSize = 2048;
        this.bufferLength = this.analyser.frequencyBinCount;
        this.dataArray = new Uint8Array(this.bufferLength);
        const canvas = this.$refs.visualizerCanvas;
        this.canvasContext = canvas.getContext("2d");
        this.isVisualizing = true;
        this.draw();
      } catch (error) {
        console.error("Microphone access denied:", error);
      }
    },
    stopRecording() {
      if (this.mediaRecorder) {
        this.mediaRecorder.stop();

        // 시각화 중지
        this.isVisualizing = false;

        // 오디오 스트림 중지
        if (this.stream) {
          const tracks = this.stream.getTracks();
          tracks.forEach((track) => track.stop());
        }

        // 오디오 컨텍스트 종료
        if (this.audioContext && this.audioContext.state !== "closed") {
          this.audioContext.close();
        }

        // 시각화 종료 시, Canvas 초기화
        if (this.canvasContext) {
          this.canvasContext.clearRect(
            0,
            0,
            this.canvasContext.canvas.width,
            this.canvasContext.canvas.height
          );
        }

        // 녹음된 오디오를 blob에 저장해서 서버로 보냄.
        //this.handleStop();

        this.isRecording = 3;
      }
    },
    handleStop() {
      // This function is called when mediaRecorder stops
      const blob = new Blob(this.recordedChunks,{ type: "audio/wav" });
      console.log("Recorded Chunks Length:", this.recordedChunks.length);
      this.uploadAudio(blob);
    },
    async uploadAudio(blob) {
      const formData = new FormData();
      formData.append("audio", blob, "recording.wav");
      console.log("Blob type:", blob.type);
      try {
        const response = await axios.post(
          `${process.env.VUE_APP_DJANGO_APP_BACK_END_URL}/interview/speach_text`,
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          }
        );
        console.log("Server response:", response.data.result);
        // Handle server response
        this.sttResult = response.data.result;
      } catch (error) {
        console.error("Error uploading audio:", error);
      }
    },
    doneRecord() {
      this.isRecognitionComplete = true;
    },
    draw() {
      if (!this.isVisualizing) return;
      requestAnimationFrame(this.draw);
      this.analyser.getByteTimeDomainData(this.dataArray);
      this.canvasContext.clearRect(
        0,
        0,
        this.canvasContext.canvas.width,
        this.canvasContext.canvas.height
      );
      this.canvasContext.lineWidth = 2;
      this.canvasContext.strokeStyle = "#07d0a9";
      this.canvasContext.beginPath();
      const sliceWidth =
        (this.canvasContext.canvas.width * 1.0) / this.bufferLength;
      let x = 0;
      for (let i = 0; i < this.bufferLength; i++) {
        const v = this.dataArray[i] / 128.0;
        const y = (v * this.canvasContext.canvas.height) / 2;
        if (i === 0) {
          this.canvasContext.moveTo(x, y);
        } else {
          this.canvasContext.lineTo(x, y);
        }
        x += sliceWidth;
      }
      this.canvasContext.lineTo(
        this.canvasContext.canvas.width,
        this.canvasContext.canvas.height / 2
      );
      this.canvasContext.stroke();
    },
  },
  beforeUnmount() {
    this.stopRecording(); // 컴포넌트가 언마운트되기 전에 녹음 중지
  },
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

.AI-Setting {
  max-width: 1280px;
  margin: 0 auto;
  padding: 20px;
  background-color: #0c1b49;
}

.AI-interview-title {
  color: #ffffff;
  background-color: #1a2b3c;
  padding: 10px;
  margin-bottom: 20px;
  border: none;
  border-radius: 5px;
}

.device-check {
  background-color: #ffffff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  color: black;
}

.webcam-container {
  width: 640px;
  margin: 0 auto;
}

.webcam-frame {
  position: relative;
  width: 100%;
  padding-top: 75%;
  overflow: hidden;
  background-color: #000;
}

.webcam-frame video {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.recognition-status {
  position: absolute;
  top: 10px;
  left: 10px;
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  padding: 5px 10px;
  border-radius: 5px;
}

.image-container {
  transform: translate(-50%, -50%);
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.mic-test-gif {
  max-width: 100%;
  max-height: 100%;
}

.device-check-btn {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: bold;
  width: 200px;
  height: 50px;
  font-size: 1.1rem;
  transition:
    background-color 0.5s,
    color 0.5s;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.btn-pre {
  background-color: #07d0a9;
  color: #ffffff;
}

.btn-pre:hover {
  background-color: #004e4a;
}

.btn-next {
  background-color: #1659de;
  color: #ffffff;
}

.btn-next:hover {
  background-color: #003d8c;
}

.btn-recognition {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  background-color: #1659de;
  color: #ffffff;
  transition: background-color 0.3s ease;
}

.btn-recognition:hover {
  background-color: #003d8c;
}

.btn-recording {
  background-color: #ff0000 !important;
  color: #ffffff !important;
}

.btn-recording:hover {
  background-color: #cc0000 !important;
}

.btn-success {
  background-color: #32cd32 !important;
  color: #ffffff !important;
}

.btn-success:hover {
  background-color: #2ab02a !important;
}

.btn-retry {
  background-color: #ffa500 !important;
  color: #ffffff !important;
}

.btn-retry:hover {
  background-color: #ff8c00 !important;
}

.btn-recognition:disabled {
  background-color: #32cd32;
  color: #ffffff;
  cursor: not-allowed;
  opacity: 0.9;
}

.fade-enter-active,
.fade-leave-active {
  transition:
    opacity 0.5s,
    transform 0.5s;
}

.fade-enter,
.fade-leave-to {
  opacity: 0;
  transform: translateY(10px);
}

.audio-canvas {
  border: 1px solid black;
  background-color: #000;
}

.canvas-container {
  width: 640px;
  margin: 0 auto;
}

.canvas-frame {
  position: relative;
  overflow: hidden;
  display: flex; /* Flexbox 컨테이너로 설정 */
  justify-content: center; /* 수평 중앙 정렬 */
  align-items: center; /* 수직 중앙 정렬 */
  height: 480px; /* 캔버스의 높이와 맞춰야 합니다. */
}

.re-or-done {
  position: absolute;
  bottom: 20px;
  left: 50%;
  display: flex;
  transform: translateX(-50%);
}
.re-or-done button {
  margin: 0 10px 0 10px;
}

.stt-result {
  font-size: 24px;
  color: white;
  position: absolute;
  text-align: center;
  top: 40%;

  margin: auto;
}
</style>
