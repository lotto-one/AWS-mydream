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
      <h3 class="AI-interview-title">기본면접이 곧 시작됩니다</h3>
      <h5>
        <strong style="color: mediumblue">인성면접</strong>과
        <strong style="color: mediumblue">인성+직무면접</strong> 중 원하는
        유형을 선택하세요.
      </h5>
      <p>면접 유형을 선택 후 다음 버튼을 누르세요.</p>
      <div class="notice-section">
        <div class="notice-item" @click="triggerCheckbox('personality')">
          <div class="checkbox-container">
            <input
              type="checkbox"
              id="personality"
              ref="personalityCheckbox"
              v-model="personalitySelected"
              @change="updateSelection(1)"
            />
            <label for="personality"></label>
          </div>
          <div class="notice-image1">
            <img src="img/interview1.png" alt="인성면접" />
          </div>
          <div class="notice-text">
            <h5>인성면접</h5>
            <p>
              <strong style="color: mediumblue">인성 관련 질문 5개</strong>로
              구성됩니다.
            </p>
          </div>
        </div>


        <!-- 기존의 notice-item -->        
        <div class="notice-item" v-if="!cnsYnN" :style="{ opacity: 0.3 }" @click="selectCNS">
          <div class="checkbox-container" v-if="cnsYnN">
            <input
              type="checkbox"
              id="personality_job"
              v-model="personalityJobSelected"              
              @change="updateSelection(2)"
            />
            <label for="personality_job"></label>
          </div>
          <div class="notice-image2">
            <img src="img/interview2.png" alt="인성+직무면접" />
          </div>
          <div class="notice-text">
            <h5>인성+직무면접</h5>
            <p>
              <strong style="color: mediumblue">인성 관련 질문 5개</strong>와
              <strong style="color: mediumblue">직무 관련 질문 2개</strong>로
              구성됩니다.
            </p>
            <p>
              인성+직무면접은
              <strong style="color: mediumblue"
                >지원 분야의 컨설턴트가 배정된 경우에만 진행 가능</strong
              >
              합니다.
            </p>
          </div>
        </div>
        <div class="notice-item" v-show="cnsYnN" @click="triggerCheckbox('personality_job')">
          <div class="checkbox-container">
            <input
              type="checkbox"
              id="personality_job"
              ref="personalityJobCheckbox"
              v-model="personalityJobSelected"              
              @change="updateSelection(2)"
            />
            <label for="personality_job"></label>
          </div>
          <div class="notice-image2">
            <img src="img/interview2.png" alt="인성+직무면접" />
          </div>
          <div class="notice-text">
            <h5>인성+직무면접</h5>
            <p>
              <strong style="color: mediumblue">인성 관련 질문 5개</strong>와
              <strong style="color: mediumblue">직무 관련 질문 2개</strong>로
              구성됩니다.
            </p>
            <p>
              인성+직무면접은
              <strong style="color: mediumblue"
                >지원 분야의 컨설턴트가 배정된 경우에만 진행 가능</strong
              >
              합니다.
            </p>
          </div>
        </div>



      </div>
      <br />
      <h5>
        각 질문마다 30초의 생각이 주어지고 30초 후에는 답변 시간이 주어집니다.
      </h5>
      <h5>
        답변 시간은 90초이며 그 전이라도 답변이 완료되면 답변 제출 버튼을 누르면
        됩니다.
      </h5>
      <h5><strong style="color: mediumblue">답변 시간 20초 이내에서 1회에 한하여 다시 답변할 수 있습니다.</strong></h5>
      
      <!-- Buttons -->
      <div class="device-check-btn">
        <button class="btn btn-pre" @click="handleBack">〈 이전</button>
        <transition name="fade" mode="out-in">
          <button
            v-if="isInterviewSelected"
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
import axios from 'axios';
export default {
  data() {
    return {
      progress: 60,
      currentStep: 3,
      personalitySelected: false,
      personalityJobSelected: false,
      selectedInterview: "",
      inttypecd:0,
      memno:0,
      cnsYnN:'',
    };
  },
  mounted() {    
    window.scrollTo(0, 0);
    this.memno = localStorage.getItem('memno');
    // 컨설턴트 존재 여부 Y/N으로 반환
    axios.get(`${process.env.VUE_APP_BACK_END_URL}/interview/getcnsYnN?memno=${this.memno}`)   
    .then((res) => {    
      const status = res.data;
      console.log("하",res.data);
      console.log(status);
      localStorage.setItem('status',status);
      if (status=='N'){
        this.cnsYnN = false;
      } else if(status=='Y') {     
        this.cnsYnN = true;
      }
      localStorage.removeItem('status');
    })
    .catch((error) => {
      console.error("마운트시 에러발생");
    });      
  },
  computed: {
    isInterviewSelected() {
      return this.personalitySelected || this.personalityJobSelected;
    },
  },
  methods: {
    handleBack() {
      // this.$router.push({ name: "AIMicTest" });
      history.back();
    },
    handleNext() {
      console.log('inttypecd',this.inttypecd);
      // 인터뷰타입이름 인성/직무로 반환
      axios.get(`${process.env.VUE_APP_BACK_END_URL}/interview/getinttypename?inttypecd=${this.inttypecd}`)
      .then((res) => {  
        const inttypename = res.data;
        console.log('직무or인성',inttypename);
        localStorage.setItem('inttypename',inttypename);
        
      })
      .catch((error) => {
        console.error("에러발생 에러발생");
      });
      // 질문 받아오기.
      axios.get(`${process.env.VUE_APP_BACK_END_URL}/interview/getquestion?memno=${this.memno}`)      
        .then((res) => {          
          
          localStorage.setItem('questionlist', JSON.stringify(res.data));
          
          console.log('localStorage에 questionlist이름으로 ')
          console.log(JSON.parse(localStorage.getItem('questionlist')))
          console.log('저장')
          this.$router.push({ name: "AIInterview1" });
        })
        .catch((error) => {
          console.error("에러발생 에러발생");
        });
      
    },
    updateSelection(inttypecd) {
      if (this.personalitySelected && this.personalityJobSelected) {
        // 둘 다 선택된 경우, 마지막으로 선택된 것만 유지
        if (this.selectedInterview === "personality") {
          this.selectedInterview = "personality_job";
          this.personalitySelected = false;
        } else {
          this.selectedInterview = "personality";
          this.personalityJobSelected = false;
        }
      } else if (this.personalitySelected) {
        this.selectedInterview = "personality";
      } else if (this.personalityJobSelected) {
        this.selectedInterview = "personality_job";
      } else {
        this.selectedInterview = "";
      }
      // 인성면접 체크박스를 누를 때 마다 로컬스토리지의 inttypecd에 1이나 2가 업데이트됨.
      localStorage.setItem('inttypecd', inttypecd); 
      console.log('localStorage에 inttypecd이름으로 <',localStorage.getItem('inttypecd'),'>저장')      
      this.inttypecd = inttypecd;
      
    },
    triggerCheckbox(type) {
      if (type === 'personality') {
        this.$refs.personalityCheckbox.click(); // 체크박스를 클릭한 것처럼 동작
      } else if (type === 'personality_job') {
        this.$refs.personalityJobCheckbox.click(); // 체크박스를 클릭한 것처럼 동작
      }
    },
    selectCNS(){
      alert('컨설턴트를 선택해주세요')
    }
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
  color:black;
}

.notice-section {
  display: flex;
  justify-content: space-between;
  gap: 20px;
  margin-top: 20px;
}

.notice-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: #f8f9fa;
  border-radius: 8px;
  padding: 15px;
  align-items: center;
  position: relative;
}

.notice-image1,.notice-image2 {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-bottom: 15px;
}

.notice-image1 img {
  width: 240px;
  height: auto;
  object-fit: contain;
}

.notice-image2 img {
  width: 250px;
  height: auto;
  object-fit: contain;
}

.notice-text {
  text-align: center;
  margin-bottom: 15px;
}

.notice-text h5 {
  margin-bottom: 10px;
  font-size: 1.1rem;
  color: #333;
}

.notice-text p {
  margin: 0;
  font-size: 0.9rem;
  color: #666;
}

.checkbox-container {
  position: absolute;
  top: 10px;
  right: 10px;
}

.checkbox-container input[type="checkbox"] {
  display: none;
}

.checkbox-container label {
  display: inline-block;
  width: 20px;
  height: 20px;
  background-color: #fff;
  border: 2px solid #ddd;
  border-radius: 3px;
  cursor: pointer;
}

.checkbox-container input[type="checkbox"]:checked + label {
  background-color: #32cd32;
  border-color: #32cd32;
}

.checkbox-container input[type="checkbox"]:checked + label:after {
  content: "\2714";
  color: white;
  position: absolute;
  top: 0;
  left: 3px;
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
  transition: background-color 0.5s, color 0.5s;
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

.device-check-btn {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s, transform 0.5s;
}

.fade-enter,
.fade-leave-to {
  opacity: 0;
  transform: translateY(10px);
}

</style>
  