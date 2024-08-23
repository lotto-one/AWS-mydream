<template>
  <div class="container">
    <div class="mainpage-title">
      <blockquote class="mainpage-profile">
        <b> <p>ONLINE AI INTERVIEW REPORT</p></b>
      </blockquote>
    </div>
    <div class="mains-header-container">
      <div class="mains-headers-left">
        <div class="mains-headers-lefts">
          <div
            style="display: flex; justify-content: center; align-items: center"
          >
            <img
              :src="profileImageUrl"
              class="center-img"
              alt="Profile Image"
              @error="handleImageError"
            />
          </div>
          <div>
            <p class="mains-headers-p">{{ memberData.name }}</p>
          </div>
          <div>
            <table class="mains-headers-table">
              <tr>
                <td class="mains-headers-td">지원분야</td>
                <td class="mains-headers-td-a">{{ memberData.categcd }}</td>
              </tr>
              <tr>
                <td class="mains-headers-td">핸 드 폰</td>
                <td class="mains-headers-td-a">{{ memberData.phonenum }}</td>
              </tr>
              <tr>
                <td class="mains-headers-td">이 메 일</td>
                <td class="mains-headers-td-a">{{ memberData.email }}</td>
              </tr>
              <tr>
                <td class="mains-headers-td">거주지역</td>
                <td class="mains-headers-td-a">{{ memberData.loccd }}</td>
              </tr>
            </table>
          </div>
        </div>
        <div class="mains-headers-middle">
          <div class="mains-headers-middle-top">
            <div class="analysis-head">분석 결과</div>
            <div style="display: flex">
              <div>
                <div class="analysis-left">
                  스트레스
                  <div class="progress-container">
                    <div
                      class="main-progress-bar"
                      :data-value="stressRate"
                    ></div>
                  </div>
                  <div class="analysis-rate">{{ stressRate }}점</div>
                </div>
                <div class="analysis-left">
                  음성분석
                  <div class="progress-container">
                    <div
                      class="main-progress-bar"
                      :data-value="voiceRate"
                    ></div>
                  </div>
                  <div class="analysis-rate">{{ voiceRate }}점</div>
                </div>
                <div class="analysis-left">
                  자세분석
                  <div class="progress-container">
                    <div
                      class="main-progress-bar"
                      :data-value="postureRate"
                    ></div>
                  </div>
                  <div class="analysis-rate">{{ postureRate }}점</div>
                </div>
                <div class="analysis-left">
                  컨설턴트 평가
                  <div class="progress-container" style="margin-left: 8.5px">
                    <div
                      class="main-progress-bar"
                      :data-value="interviewReport.cnsscore"
                    ></div>
                  </div>
                  <div class="analysis-rate">
                    {{ interviewReport.cnsscore }}점
                  </div>
                </div>
              </div>
            </div>
          </div>
          <!-- 핵심 키워드 부분 -->
          <div class="mains-headers-middle-bottom">
            <div>
              <div class="mains-result-head">핵심 키워드</div>
              <div style="margin-left: 30px; margin-top: 10px">
                <div style="display: flex; margin-bottom: 25px">
                  <div style="display: flex">
                    <img src="img/res_stress.png" class="res-img" />
                    <p class="res-text">{{ keywordStressLevel }}</p>
                  </div>
                  <div style="display: flex; margin-left: 20px">
                    <img src="img/ress_audio.png" class="res-img" />
                    <p class="res-text">{{ keywordVoiceStability }}</p>
                  </div>
                </div>
                <div style="display: flex">
                  <div style="display: flex">
                    <img src="img/res_po.png" class="res-img" />
                    <p class="res-text">{{ keywordPostureBalance }}</p>
                  </div>
                  <div style="display: flex; margin-left: 20px">
                    <img src="img/res_resume.png" class="res-img" />
                    <p class="res-text">{{ keywordConsultantMsg }}</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="mains-headers-right">
          <div class="mains-headers-right-top">
            <div class="mains-result-head">Dates to Remember</div>
            <div style="margin-left: 20px; margin-top: 10px; font-size: 1.1rem">
              <div
                v-for="schedule in upcomingSchedules"
                :key="schedule.schno"
                style="display: flex; margin-bottom: 15px; align-items: center"
              >
                <div
                  style="
                    width: 60px;
                    text-align: center;
                    font-weight: bold;
                    color: #007bff;
                    margin-left: 5px;
                  "
                >
                  D-{{ schedule.dday }}
                </div>
                <div>
                  <div style="font-weight: bold; margin-left: 20px">
                    {{ schedule.title }}
                    <span
                      style="
                        font-size: 0.9em;
                        color: #6c757d;
                        margin-left: 30px;
                      "
                      >{{ schedule.content }}</span
                    >
                  </div>
                </div>
              </div>
            </div>
          </div>
          <!-- 컨설턴트 정보 -->
          <div class="mains-headers-right-bottom">
            <div class="main-con-head">My Consultant</div>
            <div
              v-if="consultantDetail && consultantDetail.consultant"
              style="
                display: flex;
                align-items: center;
                justify-content: space-evenly;
              "
            >
              <img
                :src="ConsultantImageUrl"
                class="center-img2"
                alt="Consultant Image"
                @error="handleImageError"
              />
              <div class="main-con-name" @click="goToConsultantChat">
                {{ consultantDetail.consultant.name }} 컨설턴트
                <div class="chat">➕</div>
                <p
                  style="
                    width: 100px;
                    position: absolute;
                    margin-left: -30px;
                    margin-top: -14px;
                    font-size: 1.9rem;
                  "
                >
                  🪪
                </p>
                <p style="font-size: 0.8em; color: #6c757d; margin-left: 40px">
                  {{ consultantDetail.consultant.categcd }} 전문
                </p>
              </div>
            </div>
            <div v-else>
              <button @click="goToConsultantInfo" class="apply-button">
                컨설턴트 신청하러 가기
              </button>
            </div>
          </div>
          <!-- 컨설턴트 정보 -->
        </div>
      </div>
    </div>
    <div>
      <div style="display: flex; gap: 20px" class="mains-tap">
        <div @click="activateSection('ai-analysis', $event)">AI종합분석</div>
        <div @click="activateSection('consulting', $event)">컨설팅</div>
      </div>
      <div class="mains-header"></div>
    </div>

    <!-- AI종합분석탭 -->
    <div
      id="ai-analysis"
      class="tab-content"
      v-if="activeSection === 'ai-analysis'"
    >
      <div class="mains-content chart-container">
        <div class="mains-floor-1">
          <div class="box2">
            <p class="box-text">감정 분석 결과</p>
            <div id="chart-1" style="margin-top: -10px"></div>
          </div>
          <div class="box5">
            <p class="box-text">음성 분석 결과</p>
            <div id="chart-2" style="margin-top: -15px"></div>
          </div>
        </div>
        <div class="mains-floor-2">
          <div class="box3">
            <p class="box-text">자세 분석 결과</p>
            <div id="chart-3" style="margin-top: -10px"></div>
          </div>
          <div class="box6">
            <p class="box-text">감정, 음성, 자세 요약</p>
            <div id="chart-4" style="margin-top: -10px"></div>
          </div>
          <!-- 블러 오버레이 -->
          <div v-if="showBlurOverlay" class="blur-overlay">
            <p>
              인터뷰 데이터가 없습니다. <br />
              <button @click="goToAIInterview" class="apply-button">
                AI 면접 하러가기
              </button>
            </p>
          </div>
          <!-- 블러 오버레이 끝 -->
        </div>
      </div>
    </div>
    <!-- 컨설팅 탭 -->
    <div
      id="consulting"
      class="tab-content chart-container"
      v-if="activeSection === 'consulting'"
    >
      <div
        class="mains-content"
        style="
          display: flex;
          gap: 20px;
          align-items: center;
          justify-content: space-evenly;
        "
      >
        <div>
          <!-- 직무면접 1번 -->
          <div class="box10-2">
            <div class="question-container">
              <p class="box-text3">직무면접 1번</p>
              <p class="interview-q">
                {{ interviewReport.jobquestion1 || "질문 없음" }}
              </p>
            </div>
            <div class="answer-container">
              <p class="box-text2">나의 답변</p>
              <p class="interview-a">
                {{ interviewReport.jobanswer1 || "답변 없음" }}
              </p>
            </div>
          </div>

          <!-- 직무면접 2번 -->
          <div class="box10-2">
            <div class="question-container">
              <p class="box-text3">직무면접 2번</p>
              <p class="interview-q">
                {{ interviewReport.jobquestion2 || "질문 없음" }}
              </p>
            </div>
            <div class="answer-container">
              <p class="box-text2">나의 답변</p>
              <p class="interview-a">
                {{ interviewReport.jobanswer2 || "답변 없음" }}
              </p>
            </div>
          </div>
        </div>

        <div>
          <!-- 피드백 1 -->
          <div class="box10-2">
            <div class="question-container">
              <p class="box-text3">컨설턴트 피드백</p>
              <hr class="box-line" />
              <p class="interview-f">
                {{ consultantfeedback.feedback1 || "피드백 없음" }}
              </p>
            </div>
          </div>
          <!-- 피드백 2 -->
          <div class="box10-2">
            <div class="question-container">
              <p class="box-text3">컨설턴트 피드백</p>
              <hr class="box-line" />
              <p class="interview-f">
                {{ consultantfeedback.feedback2 || "피드백 없음" }}
              </p>
            </div>
          </div>
        </div>
      </div>

      <!-- 직무면접 종합분석 -->
      <div class="box9-2">
        <p class="box-text">컨설턴트 평가 종합 요약</p>
        <hr class="box-line" />
        <p class="interview-a-p">
          {{ consultantTotalFeedback || "종합 요약 내용 없음" }}
        </p>
        <!-- 블러 오버레이 -->
        <div v-if="showBlurOverlay" class="blur-overlay">
          <p>
            인터뷰 데이터가 없습니다. <br />
            <button @click="goToAIInterview" class="apply-button">
              AI 면접 하러가기
            </button>
          </p>
        </div>
        <!-- 블러 오버레이 끝 -->
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";
import Highcharts from "highcharts";
import HighchartsMore from "highcharts/highcharts-more";

HighchartsMore(Highcharts);

export default {
  props: {
    activeSection: String,
  },
  setup() {
    const getMemno = () => {
      const memno = localStorage.getItem("memno");
      if (!memno) {
        console.error("No memno found in localStorage");
        // 사용자를 로그인 페이지로 리디렉션
        router.push("/login");
      }
      return memno;
    };
    const memberData = ref({});
    const intno = ref(null);
    const cnsno = ref(null);

    // 가장 최근 인터뷰 번호와 컨설턴트 번호를 가져오는 함수
    const fetchLatestInterviewInfo = async (memno) => {
      try {
        const response = await axios.get(
          `${process.env.VUE_APP_BACK_END_URL}/mainpage/latestInterviewInfo?memno=${memno}`
        );
        console.log("API Response:", response.data, "Status:", response.status);
        if (
          response.status === 204 ||
          !response.data ||
          Object.keys(response.data).length === 0
        ) {
          console.warn("No interview data found");
          intno.value = null;
          cnsno.value = null;
        } else if (response.data.intno) {
          intno.value = response.data.intno;
          cnsno.value = response.data.cnsno;
        } else {
          console.warn("Unexpected data structure:", response.data);
          intno.value = null;
          cnsno.value = null;
        }
      } catch (error) {
        console.error("Error fetching latest interview info:", error);
        intno.value = null;
        cnsno.value = null;
        throw error;
      }
    };

    const showBlurOverlay = computed(() => {
      console.log("intno value:", intno.value); // 디버깅을 위한 로그
      return intno.value === null || intno.value === undefined;
    });

    const categoryMap = {
      1: "IT/개발",
      2: "교육",
      3: "영업/마케팅",
      4: "기획/전략",
      5: "경영",
    };
    const locationMap = {
      1: "서울",
      2: "경기도",
      3: "충청도",
      4: "전라도",
      5: "경상도",
      6: "강원도",
      7: "제주도",
    };
    const interviewReport = ref({
      jobquestion1: "",
      jobanswer1: "",
      jobquestion2: "",
      jobanswer2: "",
    });
    const stressRate = ref(0);
    const voiceRate = ref(0);
    const postureRate = ref(0);
    const memberSchedules = ref([]);
    const activeSection = ref("ai-analysis");
    const consultantfeedback = ref({
      feedback1: "",
      feedback2: "",
    });
    const consultantTotalFeedback = ref("");
    const loading = ref(false);
    const error = ref(null);
    const consultantDetail = ref(null);
    const router = useRouter();

    // 이미지 로드 실패 시 대체 이미지 설정
    const handleImageError = (event) => {
      event.target.src = "img/MainPage_image/noimg.png";
    };

    // 회원에게 매칭된 컨설턴트 없을 때, 컨설턴트 신청 페이지로 이동
    const goToConsultantInfo = () => {
      router.push("/ConsultantInfo");
    };

    // 컨설턴트 1대1 상담으로 가기
    const goToConsultantChat = () => {
      router.push("/MyConsultantProfile");
    };

    // 인터뷰 데이터가 없을 경우, AI면접으로 가기
    const goToAIInterview = () => {
      router.push("/AISetting");
    };

    // 회원 데이터 변환 함수(희망직무, 거주지역)
    const transformMemberData = (data) => {
      if (data) {
        return {
          ...data,
          categcd: categoryMap[data.categcd] || "알 수 없음",
          loccd: locationMap[data.loccd] || "알 수 없음",
        };
      }
      return null;
    };

    // 회원사진 가져오기
    const profileImageUrl = computed(() => {
      if (memberData.value && memberData.value.imgname) {
        return `http://localhost/mydream/${memberData.value.imgname}`;
      }
      return "img/MainPage_image/noimg.png"; // 기본 이미지 경로
    });

    // 회원정보 가져오기
    const fetchMemberData = async (memno) => {
      try {
        const response = await axios.get(
          `${process.env.VUE_APP_BACK_END_URL}/mainpage/memberDetail?memno=${memno}`
        );
        memberData.value = transformMemberData(response.data);
        // console.log("회원정보:", memberData.value);
      } catch (error) {
        console.error("Error fetching member data:", error);
      }
    };

    // 스트레스율
    const fetchStressRate = async (intno, memno) => {
      // console.log(typeof intno.value);
      try {
        const response = await axios.get(
          `${process.env.VUE_APP_BACK_END_URL}/mainpage/stressRate?intno=${intno.value}&memno=${memno}`
        );
        stressRate.value = response.data;
        // console.log("스트레스율: ", stressRate.value);
      } catch (error) {
        console.error("Error fetching stress rate:", error);
      }
    };
    // 음성분석
    const fetchVoiceRate = async (intno, memno) => {
      try {
        const response = await axios.get(
          `${process.env.VUE_APP_BACK_END_URL}/mainpage/voiceRate?intno=${intno.value}&memno=${memno}`
        );
        voiceRate.value = response.data;
      } catch (error) {
        console.error("Error fetching voice rate:", error);
      }
    };
    // 자세분석
    const fetchPostureRate = async (intno, memno) => {
      try {
        const response = await axios.get(
          `${process.env.VUE_APP_BACK_END_URL}/mainpage/postureRate?intno=${intno.value}&memno=${memno}`
        );
        postureRate.value = response.data;
      } catch (error) {
        console.error("Error fetching posture bad count rate:", error);
      }
    };

    // 컨설턴트 평가점수
    const fetchConsultantScore = async (intno) => {
      const memno = getMemno();
      if (!intno.value || !memno) {
        console.error("Invalid intno or memno", { intno: intno.value, memno });
        return;
      }

      try {
        const response = await axios.get(
          `${process.env.VUE_APP_BACK_END_URL}/mainpage/consultantScore`,
          {
            params: {
              intno: intno.value,
              memno: memno,
            },
          }
        );

        if (response.data) {
          interviewReport.value = response.data;
          console.log("컨설턴트 평가점수: ", interviewReport.value);
        } else {
          console.warn("No data received from consultantScore API");
        }
      } catch (error) {
        console.error("Error fetching consultant score:", error.message);
        if (error.response) {
          console.error("Response status:", error.response.status);
          console.error("Response data:", error.response.data);
        }
      }
    };

    // 회원에게 매칭된 컨설턴트 정보 가져오기
    const fetchConsultantDetail = async (memno) => {
      try {
        const response = await axios.get(
          `${process.env.VUE_APP_BACK_END_URL}/mainpage/memberConsultantDetail?memno=${memno}`
        );
        const transformedData = transformConsultantData(
          response.data || { consultant: null }
        );
        consultantDetail.value = transformedData;
      } catch (error) {
        console.error("Error fetching consultant detail:", error);
        consultantDetail.value = { consultant: null }; // 오류 발생 시 안전한 기본값 설정
      }
    };
    // 컨설턴트 사진 URL 가져오기
    const ConsultantImageUrl = computed(() => {
      if (
        consultantDetail.value &&
        consultantDetail.value.consultant &&
        consultantDetail.value.consultant.imgname
      ) {
        console.log(consultantDetail.value.consultant.imgname);
        return `img/ConsultantInfo_image/${consultantDetail.value.consultant.imgname}`;
      }
      return "img/MainPage_image/noimg.png"; // 기본 이미지 경로
    });

    // 컨설턴트 데이터 변환 함수(전문분야)
    const transformConsultantData = (data) => {
      if (data && data.consultant) {
        return {
          ...data,
          consultant: {
            ...data.consultant,
            categcd: categoryMap[data.consultant.categcd] || "알 수 없음",
          },
        };
      }
      return data; // 변환할 수 없는 경우 원본 데이터 반환
    };

    // 회원일정
    const fetchMemberSchedules = async (memno) => {
      try {
        const response = await axios.get(
          `${process.env.VUE_APP_BACK_END_URL}/mainpage/memberSchedules?memno=${memno}`
        );
        memberSchedules.value = response.data;
      } catch (error) {
        console.error("Error fetching member schedules:", error);
      }
    };
    // 직무면접 질문&답변
    const fetchConsultantQuestions = async (intno, qnos) => {
      try {
        const response = await axios.get(
          `${
            process.env.VUE_APP_BACK_END_URL
          }/mainpage/consultantQuestions?intno=${intno.value}&qnos=${qnos.join(
            "&qnos="
          )}`
        );
        const data = response.data;

        // 질문과 답변 매핑
        const questionMap = {
          6: { key: "jobquestion1", answerKey: "jobanswer1" },
          7: { key: "jobquestion2", answerKey: "jobanswer2" },
        };

        data.forEach((item) => {
          if (questionMap[item.qno]) {
            interviewReport.value[questionMap[item.qno].key] = item.question;
            interviewReport.value[questionMap[item.qno].answerKey] =
              item.answer;
          }
        });
      } catch (error) {
        console.error("Error fetching consultant questions:", error);
      }
    };
    // 직무면접 질문&답변별 피드백
    const fetchConsultantFeedback = async (memno, cnsno, intno, qnos) => {
      try {
        if (cnsno.value === null || intno.value === null) {
          console.warn("cnsno or intno is null, skipping feedback fetch");
          return;
        }

        const qnosValue = Array.isArray(qnos) ? qnos : qnos.value || [];

        console.log(
          "Fetching feedback for memno:",
          memno,
          "cnsno:",
          cnsno.value,
          "intno:",
          intno.value,
          "qnos:",
          qnosValue
        );

        const response = await axios.get(
          `${process.env.VUE_APP_BACK_END_URL}/mainpage/consultantFeedback`,
          {
            params: {
              memno: memno,
              cnsno: cnsno.value,
              intno: intno.value,
              qnos: qnosValue.join(","),
            },
          }
        );
        const data = response.data;
        console.log("API response:", data);

        // 피드백 매핑
        const feedbackMap = {
          6: { key: "feedback1" },
          7: { key: "feedback2" },
        };

        data.forEach((item) => {
          if (feedbackMap[item.qno]) {
            consultantfeedback.value[feedbackMap[item.qno].key] =
              item.qcnsfeedbk;
          }
        });

        console.log("Updated consultantfeedback:", consultantfeedback.value);
      } catch (error) {
        console.error("Error fetching consultant feedback:", error);
        if (error.response) {
          console.error("Response data:", error.response.data);
          console.error("Response status:", error.response.status);
          console.error("Response headers:", error.response.headers);
        }
      }
    };
    // 종합피드백
    const fetchConsultantTotalFeedback = async (memno, intno) => {
      try {
        console.log(
          "Fetching feedback for memno:",
          memno,
          "intno:",
          intno.value
        );

        // intno가 null이 아닌지 확인
        if (intno.value === null) {
          console.warn("intno is null, skipping feedback fetch");
          return;
        }

        const response = await axios.get(
          `${process.env.VUE_APP_BACK_END_URL}/mainpage/consultantTotalFeedback`,
          {
            params: {
              memno: memno,
              intno: intno.value, // intno는 ref 객체이므로 .value 사용
            },
          }
        );

        console.log("API response:", response.data); // 응답 데이터 확인
        consultantTotalFeedback.value = response.data; // 응답 값을 직접 할당
      } catch (error) {
        console.error("Error fetching consultant total feedback:", error);
        if (error.response) {
          console.error("Response data:", error.response.data);
          console.error("Response status:", error.response.status);
        }
      }
    };

    // 회원 일정
    const calculateDday = (date) => {
      const today = new Date();
      const targetDate = new Date(date);
      const timeDiff = targetDate.getTime() - today.getTime();
      const dayDiff = Math.ceil(timeDiff / (1000 * 3600 * 24));
      return dayDiff;
    };

    // 디데이
    const upcomingSchedules = computed(() => {
      return memberSchedules.value
        .map((schedule) => ({
          ...schedule,
          dday: calculateDday(schedule.startdt),
        }))
        .filter((schedule) => schedule.dday >= 0)
        .sort((a, b) => a.dday - b.dday)
        .slice(0, 3); // 최대 3개의 일정만 표시
    });

    // 핵심키워드 분석
    const keywordStressLevel = computed(() => {
      if (
        intno.value === null ||
        stressRate.value === null ||
        stressRate.value === 0
      ) {
        return "데이터 없음";
      }
      // console.log("스트레스데이터:", stressRate.value);
      return stressRate.value > 40 ? "스트레스가 높음" : "스트레스 적정수준";
    });

    const keywordVoiceStability = computed(() => {
      if (
        intno.value === null ||
        voiceRate.value === null ||
        voiceRate.value === 0
      ) {
        return "데이터 없음";
      }
      // console.log("음성데이터:", voiceRate.value);
      return voiceRate.value > 70 ? "목소리가 불안정함" : "목소리가 안정적임";
    });

    const keywordPostureBalance = computed(() => {
      if (
        intno.value === null ||
        postureRate.value === null ||
        postureRate.value === 0
      ) {
        return "데이터 없음";
      }
      // console.log("자세데이터:", postureRate.value);
      return postureRate.value > 50 ? "자세가 불균형함" : "균형 잡힘";
    });

    const keywordConsultantMsg = computed(() => {
      if (
        intno.value === null ||
        !interviewReport.value ||
        interviewReport.value.cnsscore === undefined
      ) {
        return "데이터 없음";
      }
      return interviewReport.value.cnsscore > 70
        ? "매우 우수함"
        : "개선이 필요함";
    });

    // 버블차트, 스플라인차트, 바차트, 멀티바차트 시작
    const recentScores = ref(null);
    const fetchRecentInterviewScores = async (memno) => {
      try {
        const response = await axios.get(
          `${process.env.VUE_APP_BACK_END_URL}/mainpage/recentInterviewScores?memno=${memno}`
        );
        console.log("Fetched interview data:", response.data);
        recentScores.value = response.data;
        updateEmotionBubbleChart(recentScores.value);
        updateVoiceLineChart(recentScores.value);
        updatePostureChart(recentScores.value);
        updateMultiBarChart(recentScores.value);
      } catch (error) {
        console.error("Error fetching recent interview scores:", error);
        return null;
      }
    };

    // 날짜 형식 변환 함수
    const formatDate = (dateString) => {
      const date = new Date(dateString);
      return `${(date.getMonth() + 1).toString().padStart(2, "0")}/${date
        .getDate()
        .toString()
        .padStart(2, "0")}`;
    };

    // display 함수 추가
    const display = () => {
      return { display: "none" };
    };

    // 공통 함수: 날짜 정렬 및 회차 레이블 생성
    const sortInterviewsAndCreateLabels = (data) => {
      const sortedInterviews = data.interviewData.sort(
        (a, b) => new Date(a.date) - new Date(b.date)
      );
      const sessionLabels = sortedInterviews.map(
        (_, index) => `${index + 1}회`
      );
      return { sortedInterviews, sessionLabels };
    };

    // 버블차트 (감정 분석)
    const updateEmotionBubbleChart = (data) => {
      const { sortedInterviews, sessionLabels } =
        sortInterviewsAndCreateLabels(data);

      const series = sessionLabels.map((label, index) => {
        const interview = sortedInterviews[index];
        return {
          name: label,
          data: Object.entries(data.questionData[interview.intno]).map(
            ([qno, qData]) => ({
              name: qno,
              value: qData[0].emotionScore,
              good: qData[0].ecntgood,
              soso: qData[0].ecntsoso,
              bad: qData[0].ecntbad,
            })
          ),
        };
      });

      Highcharts.chart("chart-1", {
        chart: { type: "packedbubble", height: "65%" },
        title: { text: "", align: "left" },
        subtitle: { text: "최근 5회 면접 기준", align: "left" },
        tooltip: {
          useHTML: true,
          pointFormat:
            "<b>{point.name}</b><br/>점수: {point.value}<br/>Good: {point.good}<br/>Soso: {point.soso}<br/>Bad: {point.bad}",
        },
        plotOptions: {
          packedbubble: {
            minSize: "20%",
            maxSize: "60%",
            zMin: 0,
            zMax: 100,
            layoutAlgorithm: {
              gravitationalConstant: 0.05,
              splitSeries: true,
              seriesInteraction: false,
              dragBetweenSeries: true,
              parentNodeLimit: true,
            },
            dataLabels: {
              enabled: true,
              format: "{point.name}",
              style: {
                color: "black",
                textOutline: "none",
                fontWeight: "normal",
              },
            },
          },
        },
        series: series,
      });
    };

    // 스플라인차트 (음성 분석)
    const updateVoiceLineChart = (data) => {
      if (!data || !data.interviewData || !data.questionData) {
        console.error("Invalid data for voice chart");
        return;
      }

      const { sortedInterviews, sessionLabels } =
        sortInterviewsAndCreateLabels(data);

      if (sortedInterviews.length === 0) {
        console.error("No interview data available");
        return;
      }

      const firstInterview = data.questionData[sortedInterviews[0].intno];
      if (!firstInterview) {
        console.error("No question data for the first interview");
        return;
      }

      const series = Object.keys(firstInterview).map((qno) => ({
        name: `Q ${qno.slice(1)}`,
        data: sortedInterviews
          .map((interview) => {
            const questionData = data.questionData[interview.intno][qno];
            return questionData && questionData[0]
              ? questionData[0].voiceScore
              : null;
          })
          .filter((score) => score !== null),
      }));

      Highcharts.chart("chart-2", {
        chart: { type: "spline" },
        title: { text: " ", align: "center" },
        subtitle: { text: "최근 5회 면접 기준", align: "left" },
        xAxis: {
          categories: sessionLabels,
          title: { text: "" },
        },
        yAxis: {
          title: { text: "음성 점수" },
          max: 100,
        },
        tooltip: {
          formatter: function () {
            return `<b>${this.series.name}</b><br/>회차: ${this.x}<br/>점수: ${this.y}`;
          },
        },
        plotOptions: {
          spline: {
            marker: {
              enabled: true,
              radius: 4,
            },
            dataLabels: {
              enabled: true,
              format: "{point.y:.0f}", // 소수점 없는 정수로 표시
              align: "center",
              verticalAlign: "bottom",
              y: -10, // 레이블을 점 위로 조금 이동
              style: {
                fontSize: "11px",
                fontWeight: "bold",
                textOutline: "1px contrast", // 레이블 가독성 향상
              },
            },
          },
        },
        series: series,
      });
    };

    // 바차트 (자세 분석) - 면접 회차별 총합 평균 점수
    const updatePostureChart = (data) => {
      const { sortedInterviews, sessionLabels } =
        sortInterviewsAndCreateLabels(data);

      const series = [
        {
          name: "평균 자세 점수",
          data: sortedInterviews.map((interview) => {
            const questions = Object.values(data.questionData[interview.intno]);
            const avgScore =
              questions.reduce((sum, q) => sum + q[0].postureScore, 0) /
              questions.length;
            return Number(avgScore.toFixed(2));
          }),
        },
      ];

      Highcharts.chart("chart-3", {
        chart: { type: "column" },
        title: { text: "" },
        subtitle: { text: "최근 5회 면접 기준", align: "left" },
        xAxis: {
          categories: sessionLabels,
          title: { text: " " },
          crosshair: true,
        },
        yAxis: {
          title: { text: "자세 점수" },
          max: 100,
        },
        tooltip: {
          headerFormat:
            '<span style="font-size:10px">{point.key}</span><table>',
          pointFormat:
            '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
            '<td style="padding:0"><b>{point.y:.0f}</b></td></tr>',
          footerFormat: "</table>",
          shared: true,
          useHTML: true,
        },
        plotOptions: {
          column: {
            pointPadding: 0.2,
            borderWidth: 0,
            dataLabels: {
              enabled: true,
              format: "{point.y:.0f}",
              style: {
                fontWeight: "bold",
              },
            },
          },
        },
        series: series,
      });
    };

    // 멀티바차트 (종합분석:emotionScore,voiceScore,postureScore)
    const updateMultiBarChart = (data) => {
      const { sortedInterviews, sessionLabels } =
        sortInterviewsAndCreateLabels(data);

      const series = sortedInterviews.map((interview, index) => {
        const questions = Object.values(data.questionData[interview.intno]);

        // 각 항목별 평균 계산
        const avgEmotionScore =
          questions.reduce((sum, q) => sum + q[0].emotionScore, 0) /
          questions.length;
        const avgVoiceScore =
          questions.reduce((sum, q) => sum + q[0].voiceScore, 0) /
          questions.length;
        const avgPostureScore =
          questions.reduce((sum, q) => sum + q[0].postureScore, 0) /
          questions.length;

        return {
          name: `${index + 1}회`,
          data: [
            Math.round(avgEmotionScore), // 반올림하여 정수로 변환
            Math.round(avgVoiceScore),
            Math.round(avgPostureScore),
          ],
        };
      });

      Highcharts.chart("chart-4", {
        chart: { type: "column" },
        title: { text: "" },
        subtitle: { text: "최근 5회 면접 기준", align: "left" },
        xAxis: {
          categories: ["감정", "음성", "자세"],
          title: { text: " " },
          crosshair: true,
        },
        yAxis: {
          title: { text: "평균 점수" },
          max: 100,
        },
        tooltip: {
          headerFormat:
            '<span style="font-size:10px">{point.key}</span><table>',
          pointFormat:
            '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
            '<td style="padding:0"><b>{point.y}</b></td></tr>',
          footerFormat: "</table>",
          shared: true,
          useHTML: true,
        },
        plotOptions: {
          column: {
            pointPadding: 0.2,
            borderWidth: 0,
            dataLabels: {
              enabled: true,
              format: "{point.y}",
              style: {
                fontWeight: "bold",
              },
            },
          },
        },
        series: series,
      });
    };
    // 전체 차트 끝

    onMounted(async () => {
      loading.value = true;
      error.value = null;
      console.log(intno.value);
      try {
        const memno = getMemno();
        if (!memno) {
          throw new Error("No memno found in localStorage");
        }

        await fetchLatestInterviewInfo(memno);
        console.log("Fetched intno:", intno.value);

        await Promise.all([
          fetchMemberData(memno),
          fetchStressRate(intno, memno),
          fetchVoiceRate(intno, memno),
          fetchPostureRate(intno, memno),
          fetchConsultantScore(intno),
          fetchMemberSchedules(memno),
          fetchConsultantQuestions(intno, [6, 7]),
          fetchConsultantFeedback(memno, cnsno, intno, [6, 7]),
          fetchConsultantTotalFeedback(memno, intno),
          fetchConsultantDetail(memno),
          fetchRecentInterviewScores(memno),
        ]);

        // 프로그레스 바 초기화
        const progressBars = document.querySelectorAll(".main-progress-bar");
        progressBars.forEach((bar) => {
          const value = bar.getAttribute("data-value");
          bar.style.width = `${value}%`;
        });
      } catch (err) {
        console.error("Error in onMounted:", err);
        error.value = err.message;
      } finally {
        loading.value = false;
      }
    });

    // 탭 활성화
    const activateSection = (sectionId, event) => {
      event.preventDefault();
      activeSection.value = sectionId;
    };

    return {
      display,
      intno,
      cnsno,
      memberData,
      interviewReport,
      stressRate,
      voiceRate,
      postureRate,
      upcomingSchedules,
      activeSection,
      activateSection,
      keywordStressLevel,
      keywordVoiceStability,
      keywordPostureBalance,
      keywordConsultantMsg,
      consultantfeedback,
      consultantTotalFeedback,
      loading,
      error,
      consultantDetail,
      handleImageError,
      goToConsultantInfo,
      goToConsultantChat,
      goToAIInterview,
      profileImageUrl,
      ConsultantImageUrl,
      recentScores,
      showBlurOverlay,
    };
  },
};
</script>

<style scoped>
.chart-container {
  position: relative;
}

.blur-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(10px);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 10;
}

.blur-overlay p {
  font-size: 2.5rem;
  font-weight: bold;
  color: #0d0d0d;
}
</style>
