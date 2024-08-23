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
      <template v-if="!isInterviewCompleted">
        <h3 class="AI-interview-title">
          질문 7<br /><span v-show="interviewStarted">[직무 질문] {{question7.totalq}}</span>
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
      </template>

      <template v-else-if="isInterviewCompleted && !insertdone">
        <br>
        <h1 style="font-weight: bold;">면접 결과 분석중입니다 ...</h1>
        <img src="img/InterviewRes_image/waitInsert.gif">
      </template>

      <template v-else-if="insertdone">
        <div class="interview-completed">
          <h3 class="AI-interview-title">수고하셨습니다.</h3>
          <br />
          <h5>이제 모든 인성 AI면접을 마쳤습니다.</h5>
          <h5>아래의 면접 결과 버튼을 눌러 결과지를 확인해 보시기 바랍니다.</h5>
          <br />
          <button class="btn btn-result" @click="viewResults">
            면접 결과 확인
          </button>
        </div>
      </template>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      progress: 94,
      currentStep: 4,
      remainingTime: 30,
      progressTime: 100,
      interval: null,      
      isThinkingTime: false,
      isThinkingTimeOver: false,
      isAnsweringTime: false,
      restartButtonEnabled: false,
      hasRestarted: false,
      showInterviewSection: false,
      interviewStarted: false,
      question7: JSON.parse(localStorage.getItem('questionlist'))[6],
      isInterviewCompleted: false,
      mediaRecorder: null,
      recordedChunks: [],
      videoStream: null,     
      detail:[],
      stt:[],
      emotion:[],
      position:[],
      voice:[],
      video_url:[],    
      feedback:[],
      intresult:{
        sttresult:[],
        eresult:[],
        presult:[],
        vresult:[]
      },
      escoreAverage:0,
      pscoreAverage:0,
      vscoreAverage:0,
      eresultData:[
        '분석 결과, 면접 내내 안정적인 심리 상태를 유지하며, 스트레스 수준이 매우 낮았습니다.',
        '전반적으로 차분하고 침착하게 면접을 진행하였으며, 스트레스를 효과적으로 관리하는 모습이 돋보였습니다.',
        '일부 질문에서 스트레스 반응이 다소 높아졌으나, 곧 안정적인 상태로 회복되었습니다.',
        '면접 중간에 약간의 긴장감이 느껴졌지만, 전반적으로는 잘 통제된 스트레스 수준이었습니다.',
        '면접 내내 높은 스트레스율이 관찰되었으며, 긴장으로 인해 일부 답변에 영향을 미친 것으로 보입니다.',
        '전반적으로 스트레스 관리가 어려워 보였으며, 지속적인 높은 스트레스 반응이 나타났습니다.',
      ],
      presultData:[
        '분석 결과, 면접 내내 올바른 자세를 유지하며, 매우 안정적인 인상을 주었습니다.',
        '자세가 흐트러짐 없이 지속적으로 유지되어, 면접에 대한 준비와 집중력이 잘 나타났습니다.',
        '대체로 안정적인 자세를 유지했으나, 긴장한 순간에 자세가 약간 흐트러진 부분이 있었습니다.',
        '일부 순간에 자세가 약간 불안정해졌지만, 전반적으로는 큰 문제 없이 면접을 진행했습니다.',
        '면접 중에 자세가 자주 흐트러져 불안정한 인상을 주었으며, 긴장감이 느껴졌습니다.',
        '전반적으로 자세 유지에 어려움을 겪었으며, 이로 인해 면접 집중도가 떨어진 것으로 보입니다.',
      ],
      vresultData:[
        '음성 톤과 발음이 매우 안정적이며, 명확한 의사 전달이 잘 이루어졌습니다.',
        '전체적으로 자신감 있는 음성이 돋보였으며, 전달력과 설득력이 매우 좋습니다.',
        '음성이 대체로 안정적이었지만, 일부 부분에서 약간의 긴장감이 느껴졌습니다.',
        '전반적으로 잘 전달되었으나, 특정 순간에 음성이 다소 약해져 의사 전달이 약간 어려운 부분이 있었습니다.',
        '음성 톤이 일정하지 않아 불안정한 인상을 주었으며, 전달력이 부족했습니다.',
        '전체적으로 긴장감이 많이 느껴졌으며, 발음과 톤이 다소 불안정하여 의사 전달이 명확하지 않았습니다.',
      ],
      intresvo:{
        memno:localStorage.getItem('memno'),
        inttypecd:localStorage.getItem('inttypename')
      },
      detailvoList:[],
      detailvo:{},
      intfeedbkvo:{},
      intno:0,
      wehereinpage:true,
      insertdone:false,
    };
  },
  computed: {
    averageEscore() {
      const length = this.emotion.length;
      let total = 0;
      for (let i = 0; i < length; i++) {
        total += this.emotion[i].escore;
      }
      return length ? total / length : 0;
    }
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
          { text: this.question7.totalq },
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
      this.isThinkingTime = true;
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
      this.progress = 100
      console.log('제출누름')
      this.stopRecording(); // Stop the current recording
      
      setTimeout(() => {
        this.uploadRecordedVideo(); // Upload the recorded video
      }, 500);
    },
    uploadRecordedVideo() {
      this.isInterviewCompleted = true;
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
        .then(qres => {
          localStorage.setItem('q7detail', JSON.stringify(qres.data));  
          
          
          // 초기화
          this.stt = [];
          this.emotion = [];
          this.position = [];
          this.voice = [];
          this.video_url = [];
          this.feedback = []; 


          console.log("다들어있는거!!!!!",JSON.parse(localStorage.getItem("q7detail")))
          const details = ["q1detail", "q2detail", "q3detail", "q4detail", "q5detail", "q6detail", "q7detail"];
          details.forEach(key => {
              const storedDetail = JSON.parse(localStorage.getItem(key));
              
              this.stt.push(storedDetail.answer || " ");
              this.emotion.push(storedDetail.emotion || { escore: 0 });
              this.position.push(storedDetail.position || { pscore: 0 });
              this.voice.push(storedDetail.voice || { vscore: 0 });
              this.video_url.push(storedDetail.video_url || '');
              this.feedback.push(storedDetail.aifeedback || '');
          });        
          
          // emotion 배열에서 escore 값을 추출
          const emotionScores = this.emotion.map(e => e.escore);
          // position 배열에서 pscore 값을 추출
          const positionScores = this.position.map(p => p.pscore);
          // voice 배열에서 vscore 값을 추출
          const voiceScores = this.voice.map(v => v.vscore);

          // 각 배열의 평균 계산
          this.escoreAverage = this.computeAverage(emotionScores);
          this.pscoreAverage = this.computeAverage(positionScores);
          this.vscoreAverage = this.computeAverage(voiceScores);

          console.log('평균값확인')
          console.log(this.escoreAverage)
          console.log(this.pscoreAverage)
          console.log(this.vscoreAverage)
          

          this.updateResults(this.escoreAverage, this.eresultData, 'eresult');
          this.updateResults(this.pscoreAverage, this.presultData, 'presult');
          this.updateResults(this.vscoreAverage, this.vresultData, 'vresult');

          localStorage.setItem('efinalcmt',JSON.stringify(this.intresult.eresult))
          localStorage.setItem('pfinalcmt',JSON.stringify(this.intresult.presult))
          localStorage.setItem('vfinalcmt',JSON.stringify(this.intresult.vresult))
          console.log(`JSON.parse(localStorage.getItem('efinalcmt'))`,JSON.parse(localStorage.getItem('efinalcmt')));


          axios.post(`${process.env.VUE_APP_BACK_END_URL}/interview/setintres`, {
              memno: localStorage.getItem('memno'),
              inttypecd: 2
          })
          .then(setintres => {
              console.log('TBINTRES에 INSERT 완료');
              console.log('intno값 반환 => ', setintres.data);
              this.intno = setintres.data;

              // detailvoList를 초기화
              this.detailvoList = [];

              // 반복문을 통해 detailvo 객체를 생성하고 detailvoList에 추가
              for (let i = 0; i < 7; i++) {
                  let detailvo = {
                      intno: this.intno,
                      qno: i + 1,
                      aiqno: i <= 4 ? JSON.parse(localStorage.getItem('questionlist'))[i].totalqno : null,
                      cnsqno: i > 4 ? JSON.parse(localStorage.getItem('questionlist'))[i].totalqno : null,
                      question: JSON.parse(localStorage.getItem('questionlist'))[i].totalq,
                      answer: this.stt[i],
                      ecntgood: this.emotion[i].ecntgood,
                      ecntsoso: this.emotion[i].ecntsoso,
                      ecntbad: this.emotion[i].ecntbad,
                      pbadcnt: this.position[i].pbadcnt,
                      vhertz: this.voice[i].vhertz,
                      vjitter: this.voice[i].vjitter,
                      vspeed: this.voice[i].vspeed,
                      aifeedbk: this.feedback[i],
                      escore: this.emotion[i].escore,
                      pscore: this.position[i].pscore,
                      vscore: this.voice[i].vscore
                  };
                  this.detailvoList.push(detailvo);
              }
              console.log('this.detailvo', this.detailvoList);
              console.log('this.intresult.eresult => ',this.intresult.eresult[0])


              axios.post(`${process.env.VUE_APP_DJANGO_APP_BACK_END_URL}/interview/interview_result`, this.stt)
              .then(response => {
                // 서버에서 반환한 JSON 응답 처리            
                this.intresult.sttresult = response.data;
                console.log(this.intresult.sttresult);
                localStorage.setItem('sttfinalcmt',JSON.stringify(this.intresult.sttresult))
                console.log('sttfinalcmt',this.intresult.sttresult.sttresult1);

                this.intfeedbkvo.intno = this.intno
                this.intfeedbkvo.efeed1 = this.intresult.eresult[0];
                this.intfeedbkvo.efeed2 = this.intresult.eresult[1];
                this.intfeedbkvo.pfeed1 = this.intresult.presult[0];
                this.intfeedbkvo.pfeed2 = this.intresult.presult[1];
                this.intfeedbkvo.vfeed1 = this.intresult.vresult[0];
                this.intfeedbkvo.vfeed2 = this.intresult.vresult[1];
                this.intfeedbkvo.sttfeed1 = this.intresult.sttresult.sttresult1;
                this.intfeedbkvo.sttfeed2 = this.intresult.sttresult.sttresult2;
                console.log('this.intfeedbkvo => ',this.intfeedbkvo)
                
                axios.post(`${process.env.VUE_APP_BACK_END_URL}/interview/setintfeedbk`, this.intfeedbkvo)
                .then(()=>{
                  console.log('피드백인서트완료')
                  axios.post(`${process.env.VUE_APP_BACK_END_URL}/interview/setintdetail`, this.detailvoList)
                  .then(()=>{
                    console.log('디테일인서트완료')
                    this.insertdone=true;                    
                  })

                })

              })
              .catch(error => {
                console.error("There was an error!", error);
              });
              
              
          })
          .catch(error => {
              console.error('요청 중 오류 발생:', error);
          });

          
          console.log(JSON.parse(localStorage.getItem('questionlist')))
          


          
        })
        .catch(error => {
          console.error('Error uploading video:', error);
          setTimeout(() => {
            this.uploadRecordedVideo();
          }, 1000);
        });
      }
    },
    stopCamera() {
      this.stopStreaming();
    },
    viewResults() {
      this.$router.push({ name: "ResDuty" });
    },
    computeAverage(numbersArray) {
      if (numbersArray.length === 0) return 0; // 배열이 비어 있을 경우 0 반환
      const total = numbersArray.reduce((acc, num) => acc + num, 0);
      return total / numbersArray.length;
    },
    updateResults(average, resultData, resultKey) {
      // 빈 배열을 초기화
      this.intresult[resultKey] = [];

      if (average >= 70) {
        this.intresult[resultKey].push(resultData[0]);
        this.intresult[resultKey].push(resultData[1]);
        console.log(resultData[0]);
        console.log(resultData[1]);
      } else if (average >= 40) {
        this.intresult[resultKey].push(resultData[2]);
        this.intresult[resultKey].push(resultData[3]);
        console.log(resultData[2]);
        console.log(resultData[3]);
      } else {
        this.intresult[resultKey].push(resultData[4]);
        this.intresult[resultKey].push(resultData[5]);
        console.log(resultData[4]);
        console.log(resultData[5]);
      }
    }
  },
  beforeUnmount() {
    this.stopCamera();
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

/* 추가된 스타일 */
.interview-completed {
  text-align: center;
  margin-top: 20px;
}

.btn-result {
  background-color: #32cd32;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: bold;
  font-size: 1.1rem;
  transition: background-color 0.5s, color 0.5s;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.btn-result:hover {
  background-color: #2ab02a;
}

.ai-face-webcam-container {
  margin-right:50px;
}
</style>
