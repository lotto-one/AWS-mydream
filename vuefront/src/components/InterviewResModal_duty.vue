<template>
  <div class="modal-overlay" @click="$emit('close', false)">
    <div class="reshduty-container" @click.stop>
      <!-- 종합 평가 -->
      <div class="reshduty-con row scrollable-div">
        <div class="reshduty-summary">
          <div class="reshduty-total-t row">
            <div class="reshduty-total-title col-1">종합 분석</div>
            <div class="reshduty-total-subtitle col-1">- 직무 면접</div>
          </div>

          <div class="reshduty-summary-right">
            <p>이름: {{ userName }}</p>
            <p>날짜: {{ formatDate(credt) }}</p>
          </div>
        </div>

        <div class="resduty-subtitle3 col-1">
          <div class="resduty-subtitlecon3">종합 평가</div>
        </div>

        <div class="resduty-qcon7">
          <div class="reshduty-totalcom">
            <div class="resduty-analyze6-con">
              <li>{{ efeed1 }}</li>
              <li>{{ efeed2 }}</li>
              <li>{{ pfeed1 }}</li>
              <li>{{ pfeed2 }}</li>
              <li>{{ vfeed1 }}</li>
              <li>{{ vfeed2 }}</li>
              <li>{{ sttfeed1 }}</li>
              <li>{{ sttfeed2 }}</li>
            </div>
          </div>
        </div>

        <div v-if="cnsfeedbk" class="resduty-subtitle3 col-1">
          <div class="resduty-subtitlecon3">컨설턴트 평가</div>
        </div>
        <div class="resduty-qcon9">
          <div class="resduty-similartip">
            <img class="resduty-tipicon" src="\img\res_tip.png" />
            <div class="resduty-ti">
              답변에 대한 만족도를 평가해 주세요. 평가는 <span style="color: red;">모의면접 1회당 1번</span>만 가능하며, 여러 번 선택하실 경우 <span style="color: red;">마지막</span>으로 선택한 점수가 최종 등록됩니다.
            </div>
          </div>
        </div>

        <div v-if="cnsfeedbk" class="resduty-qcon7">
          <div class="reshduty-ctcom">
            <div class="resduty-analyze6-con">
              <li>{{ cnsfeedbk }}</li>
            </div>
          </div>

          <div class="itv-star-rating">
            답변 만족도 
            <star-rating 
            :increment="0.5" 
            @update:rating ="handleRatingUpdate" 
            active-color="#ffed00"
            :star-size="20" 
            :show-rating="false"
            :rounded-corners="true" 
            :border-width="3" 
            :star-points="[23,2, 14,17, 0,19, 10,34, 7,50, 23,43, 38,50, 36,34, 46,19, 31,17]"
            ></star-rating>
          </div>
        </div>       

        <div class="resduty-subtitle3">
          <div class="resduty-subtitlecon3">표정 분석 / 자세 분석</div>
        </div>

        <div class="resduty-qcon5">
          <div class="resduty-face">
            <div id="face" style="width: 400px; height: 400px"></div>
          </div>
          <div class="resduty-face-com">
            <div id="barchart"></div>
          </div>
        </div>

        <div class="resduty-subtitle3">
          <div class="resduty-subtitlecon3">키워드 / 음성 분석</div>
        </div>

        <div class="resduty-qcon8">
          <div class="resduty-similarbox">
            <figure class="highcharts-figure">
              <div id="wordcloud1"></div>
            </figure>
          </div>
          <div class="resduty-similarbox2">
            <figure class="highcharts-figure">
              <div id="voiceg"></div>
            </figure>
          </div>
        </div>

        <div class="reshduty-btncon row">
          <div
            @click="pagechage(1)"
            class="col-1"
            :class="{
              'reshduty-lbutton': true,
              'reshduty-active': activePage === 1,
            }"
          >
            Q1
          </div>
          <div
            @click="pagechage(2)"
            class="col-1"
            :class="{
              'reshduty-button': true,
              'reshduty-active': activePage === 2,
            }"
          >
            Q2
          </div>
          <div
            @click="pagechage(3)"
            class="col-1"
            :class="{
              'reshduty-button': true,
              'reshduty-active': activePage === 3,
            }"
          >
            Q3
          </div>
          <div
            @click="pagechage(4)"
            class="col-1"
            :class="{
              'reshduty-button': true,
              'reshduty-active': activePage === 4,
            }"
          >
            Q4
          </div>
          <div
            @click="pagechage(5)"
            class="col-1"
            :class="{
              'reshduty-button': true,
              'reshduty-active': activePage === 5,
            }"
          >
            Q5
          </div>
          <div
            @click="pagechage(6)"
            class="col-1"
            :class="{
              'reshduty-button': true,
              'reshduty-active': activePage === 6,
            }"
          >
            Q6
          </div>
          <div
            @click="pagechage(7)"
            class="col-1"
            :class="{
              'reshduty-button': true,
              'reshduty-active': activePage === 7,
            }"
          >
            Q7
          </div>
        </div>

        <!--면접질문-->
        <div class="row scrollable-div">
          <div class="reshduty-qcon">
            <div class="reshduty-q">
              <div class="reshduty-qnum">Q{{ i + 1 }}.</div>
              <div class="reshduty-question">{{ question }}</div>
            </div>
          </div>

          <div class="reshduty-qcontent1">
            <div class="reshduty-subtitle1 row">
              <div class="reshduty-subtitle2 col-1">면접 대답</div>
              <div class="reshduty-qcontent2">{{ answer }}</div>
            </div>
          </div>

          <div class="reshduty-qcon4" v-if="feedback[i]">
            <div class="reshduty-qcontent1">
              <div class="reshduty-subtitle1 row">
                <div class="reshduty-subtitle2 col-1">AI피드백</div>
                <div class="reshduty-qcontent2" :style="{ whiteSpace: 'pre-line' }">{{ feedback }}</div>
              </div>
            </div>
          </div>
          <div
            class="reshduty-qcontent1"
            v-if="CTFeedback[i - 5] && (i === 5 || i === 6)"
          >
            <div class="reshduty-subtitle1 row">
              <div class="reshduty-subtitle2 col-1">컨설턴트 피드백</div>
              <div class="reshduty-qcontent2">{{ CTFeedback[i - 5] }}</div>
            </div>
          </div>
        </div>


        <div class="reshdudy-btnarea">
          <button
            v-if="page > 1"
            class="reshduty-nextbtn col-1"
            @click="Previous"
          >
            이전 문항
          </button>
          <button v-if="page < 7" class="reshduty-nextbtn col-1" @click="next">
            다음 문항
          </button>
          <button class="reshduty-homebtn col-1" @click="$emit('close', false)">
            닫기
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import * as echarts from "echarts";
import StarRating from 'vue-star-rating'
export default {
  components: {
    StarRating
  },
  props: {
    intno: {
      type: Number,
      required: true,
    },
  },
  created() {
    console.log("Received intno:", this.intno);
  },
  data() {
    return {

      intno: this.$props.intno,
      activePage: 1,
      page: 1,
      question: "",
      answer: "",
      feedback: "",
      i: 0,
      content: 1,
      aifeedbk: "",
      cnsfeedbk: "",
      CTFeedback: [],
      userName: "",
      credt: "",
      pbadcnt: [],
      qnaData: [],
      qcnsfeedbk: "",
      rating: null,
      resetableRating: 2,
      currentRating: "No Rating",
      mouseOverRating: null
    };
  },
  methods: {
    fetchUserData() {
      axios
        .get(`${process.env.VUE_APP_BACK_END_URL}/itv/userData`, {
          params: { intno: this.intno },
        })
        .then((response) => {

          this.userName = response.data[0].mname;
          this.credt = response.data[0].credt;
          this.efeed1 = response.data[0].efeed1;
          this.efeed2 = response.data[0].efeed2;
          this.pfeed1 = response.data[0].pfeed1;
          this.pfeed2 = response.data[0].pfeed2;
          this.vfeed1 = response.data[0].vfeed1;
          this.vfeed2 = response.data[0].vfeed2;
          this.sttfeed1 = response.data[0].sttfeed1;
          this.sttfeed2 = response.data[0].sttfeed2;
          this.pbadcnt = response.data.pbadcnt;
          this.cnsfeedbk = response.data[0].cnsfeedbk;
          console.log(this.cnsfeedbk);
        });
    },
    formatDate(datetime) {

      return datetime.split("T")[0];
    },
    fetchQuestionData() {

      axios
        .get(`${process.env.VUE_APP_BACK_END_URL}/itv/fetchData`, {
          params: { intno: this.intno },
        })
        .then((response) => {

          this.qnaData = response.data;
          this.question = response.data[`${this.i}`].QUESTION;
          this.answer = response.data[`${this.i}`].ANSWER;
          this.feedback = response.data[`${this.i}`].AIFEEDBK;

          console.log(this.qnaData[2]);
          console.log(this.question);
          console.log(this.answer);
        })
        .catch((error) => {
          console.error("서버 오류:", error);
        });
    },

    fetchCTFeedback() {
      axios
        .get(`${process.env.VUE_APP_BACK_END_URL}/itv/fetchCTFeedback`, {
          params: { intno: this.intno },
        })
        .then((response) => {
          this.CTFeedback = response.data;
        })
        .catch((error) => {
          console.error("서버 오류:", error);
        });
    },
    next() {
      this.pagechage(this.page + 1);
      this.activePage = this.page;
    },
    Previous() {
      this.pagechage(this.page - 1);
      this.activePage = this.page;
    },
    
    pagechage(num) {
      console.log(num);

      this.page = num;
      this.activePage = num;
      this.i = num - 1;

      this.question =
        this.qnaData[`${num - 1}`]?.QUESTION || "저장된 값이 없습니다";
      this.answer =
        this.qnaData[`${num - 1}`]?.ANSWER || "저장된 값이 없습니다";
      this.feedback =
        this.qnaData[`${num - 1}`]?.AIFEEDBK || "저장된 값이 없습니다";
      this.qcnsfeedbk =
        this.qnaData[`${num - 1}`]?.QCNSFEEDBK || "저장된 값이 없습니다";

      if (num === 8) {
        this.content = 8;
      } else {
        this.content = 1;
      }
    },
    displayPage(pageNum) {
      return this.content === pageNum
        ? { display: "block" }
        : { display: "none" };
    },
    submitReview() {
      this.isReviewSubmitted = true;
    },
    face() {
      const chartContainer = document.getElementById("face");
      chartContainer.style.width = "300px";
      chartContainer.style.height = "400px";

      var myChart = echarts.init(chartContainer);

      axios
        .get(`${process.env.VUE_APP_BACK_END_URL}/itv/faceData`, {
          params: { intno: this.intno },
        })
        .then(function (response) {
          console.log([response.data]); // 응답 데이터 확인

          let option = {
            title: {
              text: "감정 점수",
            },

            tooltip: {
              trigger: "axis",
            },
            legend: {
              left: "left",
              data: ["점"],
            },
            radar: [
              {
                indicator: [
                  { text: "Q1", max: 100 },
                  { text: "Q2", max: 100 },
                  { text: "Q3", max: 100 },
                  { text: "Q4", max: 100 },
                  { text: "Q5", max: 100 },
                  { text: "Q6", max: 100 },
                  { text: "Q7", max: 100 },
                ],
                center: ["50%", "50%"],
                radius: 80,
              },
            ],
            series: [
              {
                type: "radar",
                tooltip: {
                  trigger: "item",
                },
                areaStyle: {
                  color: "#08AD94", // 영역 색상 변경
                },
                itemStyle: {
                  color: "#0A6065", // 데이터 포인트 색상 변경
                },
                data: [
                  {
                    value: response.data, // [60, 73, 85, 40, 65,75]
                    name: "감정 점수",
                  },
                ],
              },
            ],
          };
          myChart.setOption(option);
        })
        .catch(function (error) {
          console.error("서버 오류:", error);
        });
    },

    barchart() {

      axios
        .get(`${process.env.VUE_APP_BACK_END_URL}/itv/barData`, {
          params: { intno: this.intno },
        })
        .then(function (response) {
          Highcharts.chart("barchart", {
            chart: {
              type: "column",
              height: "380px",
            },
            title: {
              text: "자세 흐트러짐",
              align: "left",
            },
            subtitle: {
              text: "",
              align: "left",
            },
            xAxis: {
              categories: ["Q1", "Q2", "Q3", "Q4", "Q5", "Q6", "Q7"],
              crosshair: true,
              accessibility: {
                description: "Countries",
              },
            },
            yAxis: {
              min: 0,
              title: {
                text: "Count",
              },
            },
            tooltip: {
              valueSuffix: "(회)",
            },
            plotOptions: {
              column: {
                pointPadding: 0.2,
                borderWidth: 0,
              },
            },
            series: [
              {
                name: response.data.chartname,
                data: response.data.values,
                color: "#08AD94",
              },
            ],
          });
        });
    },
    voiceg() {
      axios
        .get(`${process.env.VUE_APP_BACK_END_URL}/itv/voicegData`,{
          params: { intno: this.intno },
        })
        .then(function (response) {
          console.log('음성:',response.data);
          Highcharts.chart("voiceg", {
            chart: {
              height: 380,
              width: 470,
            },

            title: {
              text: "음성 데이터",
              align: "left",
            },
            subtitle: {
              text: "",
              align: "left",
            },
            yAxis: {
              title: {
                text: response.data.chartname,
              },
            },
            xAxis: {
              title: {
                text: "(문제)",
              },
              accessibility: {
                rangeDescription: "(문제)",
              },
            },
            legend: {
              layout: "vertical",
              align: "right",
              verticalAlign: "middle",
            },
            plotOptions: {
              series: {
                label: {
                  connectorAllowed: false,
                },
                pointStart: 0,
                pointInterval: 10,
              },
            },
            series: [
              {
                name: "vhertz(음성높낮이)",
                data: response.data.vhertz,
              },
              {
                name: "vjitter(음성떨림)",
                data: response.data.vjitter,
              },
              {
                name: "vspeed(속도)",
                data: response.data.vspeed,
              },
            ],
            responsive: {
              rules: [
                {
                  condition: {
                    maxWidth: 500,
                  },
                  chartOptions: {
                    legend: {
                      layout: "horizontal",
                      align: "center",
                      verticalAlign: "bottom",
                    },
                  },
                },
              ],
            },
          });
        });
    },
    wordcloud() {
  axios
    .get(`${process.env.VUE_APP_BACK_END_URL}/itv/wordCloud`, {
      params: { intno: this.intno },
    })
    .then(function (response) {
      const text = response.data,
        // 특수문자와 숫자 제거 후 단어로 분할
        lines = text.replace(/[():'?0-9]+/g, "").split(/[,\. ]+/g),
        // 단어의 빈도를 계산
        data = lines.reduce((arr, word) => {
          if (word === "null" || word === "") return arr; // "null"이나 빈 문자열 제외

          let obj = Highcharts.find(arr, (obj) => obj.name === word);
          if (obj) {
            obj.weight += 1;
          } else {
            obj = {
              name: word,
              weight: 1,
            };
            arr.push(obj);
          }
          return arr;
        }, []);

      // 빈도에 따라 내림차순으로 정렬 후 상위 100개의 단어만 선택
      const top100 = data.sort((a, b) => b.weight - a.weight).slice(0, 90);

      // Highcharts를 이용해 워드클라우드 생성
      Highcharts.chart("wordcloud1", {
        chart: {
          height: 380,
          width: 470,
        },
        accessibility: {
          screenReaderSection: {
            beforeChartFormat:
              "<h5>{chartTitle}</h5><div>{chartSubtitle}</div><div>{chartLongdesc}</div><div>{viewTableButton}</div>",
          },
        },
        series: [
          {
            type: "wordcloud",
            data: top100,
            name: "Occurrences",
          },
        ],
        title: {
          text: "답변 주요 용어 분석",
        },
      });
    })
    .catch(function (error) {
      console.error("Error fetching word cloud data:", error);
    });
},


        // 별점 업데이트 처리 메서드
    handleRatingUpdate(rating) {
      console.log("Selected rating:", rating); // 선택된 별점 확인
      this.rating = rating; // rating 값을 저장
      this.setRating(rating); // 서버에 별점 전송
    },

    showCurrentRating(rating) {
      this.currentSelectedRating =
        rating === 0
          ? this.currentSelectedRating
          : "Click to select " + rating + " stars";
    },
    setCurrentSelectedRating(rating) {
      this.currentSelectedRating = "You have Selected: " + rating + " stars";
    }
  },
  mounted() {
    this.fetchCTFeedback();
    this.wordcloud();
    this.barchart();
    this.face();
    this.voiceg();
    this.fetchQuestionData();
    this.fetchUserData();
  },
  computed: {
    currentRatingText() {
      return this.rating
        ? "You have selected " + this.rating + " stars"
        : "No rating selected";
    },
    mouseOverRatingText() {
      return this.mouseOverRating
        ? "Click to select " + this.mouseOverRating + " stars"
        : "No Rating";
    }
  },
};
</script>


