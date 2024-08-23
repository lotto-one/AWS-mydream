import React, { useEffect, useState } from "react";
import "./Consultant.css";
import ReactEcharts from "echarts-for-react";
import Highcharts from "highcharts/highmaps";
import HighchartsReact from "highcharts-react-official";
import axios from "axios";

const Consultant = () => {
  const [consultantCount, setConsultantCount] = useState(0);
  const [memCnsAve, setMemCnsAve] = useState(0);
  const [reviewAve, setReviewAve] = useState(0);
  const [cntconsulting_week, setCntConsultingWeek] = useState(0);
  const [cntConsultingDay, setCntConsultingDay] = useState<any[]>([]);
  const [categCnt, setCategoryCounts] = useState<any[]>([]);
  const [reviewRange, setReviewRange] = useState<any[]>([]);
  const [daycategave, setDayCategAve] = useState<any[]>([]);

  useEffect(() => {
    // 컨설턴트 수 가져오기
    axios
      .get(`${process.env.REACT_APP_BACK_END_URL}/admin/cnsDash/getcnscount`)
      .then((response) => {
        setConsultantCount(response.data);
      })
      .catch((error) => {
        console.error("getcnscount!", error);
      });

    // 평균 회원 수 가져오기
    axios
      .get(`${process.env.REACT_APP_BACK_END_URL}/admin/cnsDash/memcns_ave`)
      .then((response) => {
        setMemCnsAve(response.data);
      })
      .catch((error) => {
        console.error("memcns_ave!", error);
      });

    // 평균 리뷰 값 가져오기
    axios
      .get(`${process.env.REACT_APP_BACK_END_URL}/admin/cnsDash/review_ave`)
      .then((response) => {
        setReviewAve(response.data);
      })
      .catch((error) => {
        console.error("review_ave!", error);
      });

    // 주간 컨설팅 횟수 가져오기
    axios
      .get(
        `${process.env.REACT_APP_BACK_END_URL}/admin/cnsDash/cntconsulting_thisweek`
      )
      .then((response) => {
        setCntConsultingWeek(response.data);
      })
      .catch((error) => {
        console.error("cntconsulting_thisweek!", error);
      });

    // 일일 컨설팅 횟수 가져오기
    axios
      .get(
        `${process.env.REACT_APP_BACK_END_URL}/admin/cnsDash/cntconsulting_day`
      )
      .then((response) => {
        setCntConsultingDay(response.data);
      })
      .catch((error) => {
        console.error("cntconsulting_day!", error);
      });

    // 일일 컨설팅 횟수 가져오기
    axios
      .get(`${process.env.REACT_APP_BACK_END_URL}/admin/cnsDash/getcategcnt`)
      .then((response) => {
        // console.log(response.data);
        setCategoryCounts(response.data);
      })
      .catch((error) => {
        console.error("cntconsulting_day!", error);
      });

    // 일일 컨설팅 횟수 가져오기
    axios
      .get(`${process.env.REACT_APP_BACK_END_URL}/admin/cnsDash/reviewrange`)
      .then((response) => {
        // console.log(response.data);
        setReviewRange(response.data);
      })
      .catch((error) => {
        console.error("cntconsulting_day!", error);
      });

    // 일일 컨설팅 횟수 가져오기
    axios
      .get(`${process.env.REACT_APP_BACK_END_URL}/admin/cnsDash/daycategave`)
      .then((response) => {
        console.log(response.data);
        setDayCategAve(response.data);
      })
      .catch((error) => {
        console.error("cntconsulting_day!", error);
      });
  }, []);
  /* 분야 별 점수 여긴 데이터를 뭐 넣을지 못 정했습니다.*/
  // g3 차트의 설정을 반환하는 함수
  const getG3Options = () => {
    // reviewRange 상태에서 데이터를 가져와서 차트에 맞게 변환합니다.
    const data = reviewRange.map((item) => ({
      value: item.COUNT_PER_RANGE,
      name: item.SCORE_RANGE,
    }));

    return {
      tooltip: {
        trigger: "item",
      },
      legend: {
        top: "5%",
        left: "center",
      },
      series: [
        {
          name: "점수 분포도",
          type: "pie",
          radius: ["40%", "70%"],
          center: ["50%", "70%"],
          startAngle: 180,
          endAngle: 360,
          data: data,
        },
      ],
    };
  };

  const g3 = getG3Options(); // 차트 설정을 가져옵니다.
  /* 월 평균 그래프 */

  const processChartData = () => {
    const categories = ["IT/개발", "경영", "교육", "영업/마케팅", "기획/전략"];
    const weekdays = [
      "월요일",
      "화요일",
      "수요일",
      "목요일",
      "금요일",
      "토요일",
      "일요일",
    ];

    const seriesData = categories.map((category) => ({
      name: category,
      type: "line",
      data: new Array(7).fill(0), // Initialize with zeroes for each day of the week
    }));

    seriesData.push({
      name: "평균",
      type: "line",
      data: new Array(7).fill(0), // Initialize with zeroes for each day of the week
    });

    daycategave.forEach((item) => {
      const dayIndex = weekdays.indexOf(item.WEEKDAY);
      const categIndex = categories.indexOf(item.CATEGCD_GROUP);

      if (dayIndex >= 0 && categIndex >= 0) {
        seriesData[categIndex].data[dayIndex] = item.CTG_DT_AVGSCORE;
      }

      const avgIndex = seriesData.length - 1;
      const avgScores = seriesData
        .slice(0, -1)
        .map((series) => series.data[dayIndex]);
      seriesData[avgIndex].data[dayIndex] =
        avgScores.filter((score) => score !== null).reduce((a, b) => a + b, 0) /
        avgScores.length;
    });

    return seriesData;
  };

  const monavg = {
    title: {
      text: "Line Chart",
    },
    tooltip: {
      trigger: "axis",
    },
    legend: {
      data: ["IT/개발", "경영", "교육", "영업/마케팅", "기획/전략", "평균"],
    },
    grid: {
      left: "3%",
      right: "4%",
      bottom: "3%",
      containLabel: true,
    },
    toolbox: {
      feature: {
        saveAsImage: {},
      },
    },
    xAxis: {
      type: "category",
      boundaryGap: false,
      data: [
        "월요일",
        "화요일",
        "수요일",
        "목요일",
        "금요일",
        "토요일",
        "일요일",
      ],
    },
    yAxis: {
      type: "value",
    },
    series: processChartData(),
  };

  const option1 = {
    xAxis: {
      type: "category",
      boundaryGap: false,
      data: ["20일", "21일", "22일", "23일", "24일", "25일", "26일"],
    },
    yAxis: {
      type: "value",
    },
    series: [
      {
        data: [320, 632, 301, 1334, 890, 1330, 1320],
        type: "line",
        areaStyle: {},
      },
    ],
  };

  // 데이터 변환 함수
  const transformData = (data: any[]) => {
    return data.map((item) => ({
      value: item.COUNT_PER_CATEGORY,
      name: item.CATEGCD,
    }));
  };

  // ECharts 설정
  const chartpie = {
    series: [
      {
        name: "Access From",
        type: "pie",
        radius: ["40%", "70%"],
        avoidLabelOverlap: false,
        padAngle: 5,
        itemStyle: {
          borderRadius: 10,
        },
        label: {
          show: true,
          position: "inside",
          fontWeight: "bold",
          fontSize: 16,
          rich: {
            alignCenter: {
              align: "center",
            },
          },
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 20,
            fontWeight: "bold",
          },
        },
        labelLine: {
          show: false,
        },
        data: transformData(categCnt),
      },
    ],
  };
  const chartbar = {
    tooltip: {
      trigger: "axis",
      axisPointer: {
        type: "cross",
        crossStyle: {
          color: "#999",
        },
      },
    },

    xAxis: [
      {
        type: "category",
        data: ["IT", "사무직", "제조업", "의료", "회계"],
        axisPointer: {
          type: "shadow",
        },
      },
    ],
    yAxis: [
      {
        type: "value",

        min: 0,
        max: 100,
        interval: 25,
        axisLabel: {
          formatter: "{value} 점",
        },
      },
      {
        type: "value",
        min: 0,
        max: 100,
        interval: 25,
        axisLabel: {
          formatter: "{value} 점",
        },
      },
    ],
    series: [
      {
        name: "인성면접",
        type: "bar",
        tooltip: {
          valueFormatter: function (value: number) {
            return (value as number) + " 점";
          },
        },
        data: [
          52.0, 64.9, 67.0, 53.2, 65.6, 46.7, 96.6, 62.2, 32.6, 40.0, 96.4,
          63.3,
        ],
      },
      {
        name: "직무면접",
        type: "bar",
        tooltip: {
          valueFormatter: function (value: number) {
            return (value as number) + " 점";
          },
        },
        data: [
          72.6, 65.9, 59.0, 96.4, 48.7, 30.7, 75.6, 82.2, 98.7, 38.8, 76.0,
          42.3,
        ],
      },
      {
        name: "Temperature",
        type: "line",
        yAxisIndex: 1,
        tooltip: {
          valueFormatter: function (value: number) {
            return (value as number) + "점";
          },
        },
        data: [
          2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2,
        ],
      },
    ],
  };
  let base = +new Date(1988, 9, 3);
  let oneDay = 24 * 3600 * 1000;
  let data = [[base, Math.random() * 300]];
  for (let i = 1; i < 20000; i++) {
    let now = new Date((base += oneDay));
    data.push([+now, Math.round((Math.random() - 0.5) * 20 + data[i - 1][1])]);
  }
  const chartzi = {
    xAxis: {
      type: "time",
      boundaryGap: true,
    },
    yAxis: {
      type: "value",
      boundaryGap: [0, "100%"],
    },
    dataZoom: [
      {
        type: "inside",
        start: 0,
        end: 20,
      },
      {
        start: 0,
        end: 20,
      },
    ],
    series: [
      {
        name: "Fake Data",
        type: "line",
        smooth: true,
        symbol: "none",
        areaStyle: {},
        data: data,
      },
    ],
  };

  // Prepare data for the chart
  const getChartData = () => {
    const daysOfWeek = [
      "일요일",
      "월요일",
      "화요일",
      "수요일",
      "목요일",
      "금요일",
      "토요일",
    ];
    const applyData = new Array(7).fill(0);
    const completeData = new Array(7).fill(0);

    cntConsultingDay.forEach((item) => {
      const index = daysOfWeek.indexOf(item.DAY_OF_WEEK);
      if (index >= 0) {
        applyData[index] = item.CNS_APPLY || 0;
        completeData[index] = item.CNS_COMPLETE || 0;
      }
    });

    const maxApply = Math.max(...applyData);
    const maxComplete = Math.max(...completeData);
    const max = Math.max(maxApply, maxComplete);

    return { daysOfWeek, applyData, completeData, max };
  };

  const { daysOfWeek, applyData, completeData, max } = getChartData();

  const twobar = {
    tooltip: {
      trigger: "axis",
      axisPointer: {
        type: "cross",
        crossStyle: {
          color: "#999",
        },
      },
    },
    toolbox: {
      feature: {
        dataView: { show: true, readOnly: false },
        magicType: { show: true, type: "bar" },
        restore: { show: true },
        saveAsImage: { show: true },
      },
    },
    legend: {
      data: ["컨설팅 신청", "컨설팅 완료"],
    },
    xAxis: [
      {
        type: "category",
        data: daysOfWeek,
        axisPointer: {
          type: "shadow",
        },
      },
    ],
    yAxis: [
      {
        type: "value",
        name: "", //y축 설명
        min: 0,
        max: max,
        interval: 50,
        axisLabel: {
          formatter: "{value} 회",
        },
      },
    ],
    series: [
      {
        name: "컨설팅 신청",
        type: "bar",
        data: applyData,
      },
      {
        name: "컨설팅 완료",
        type: "bar",
        data: completeData,
      },
    ],
  };
  return (
    <div className="consultant-container">
      <div className="consultant-header">
        <p className="consultant-text">&nbsp;Consultant DashBoard</p>
      </div>
      <br></br>
      <div className="consultant-dash">
        <div className="consultant-info1">
          <div>
            <h3 className="consultant-info-text">컨설턴트 수</h3>
            <div>
              <p className="consultant-info-textinfo">{consultantCount}명</p>
            </div>
          </div>
          <div className="consultant-info2">
            <img
              src="/img/admin_img/people.svg"
              className="consultant-svg"
            ></img>
          </div>
        </div>
        <div className="consultant-info1">
          <div>
            <h3 className="consultant-info-text">컨설턴트 평균 회원수</h3>
            <div>
              <p className="consultant-info-textinfo">{memCnsAve}명</p>
            </div>
          </div>
          <div className="consultant-info2">
            <img
              src="/img/admin_img/person.svg"
              className="consultant-svg"
            ></img>
          </div>
        </div>
        <div className="consultant-info1">
          <div>
            <h3 className="consultant-info-text">컨설턴트 평균 평점</h3>
            <div>
              <p className="consultant-info-textinfo">{reviewAve}점</p>
            </div>
          </div>
          <div className="consultant-info2">
            <img
              src="/img/admin_img/dollar.svg"
              className="consultant-svg"
            ></img>
          </div>
        </div>
        <div className="consultant-info1">
          <div>
            <h3 className="consultant-info-text">주간 컨설팅 횟수</h3>
            <div>
              <p className="consultant-info-textinfo">{cntconsulting_week}번</p>
            </div>
          </div>
          <div className="consultant-info2">
            <img
              src="/img/admin_img/dollars.svg"
              className="consultant-svg"
            ></img>
          </div>
        </div>
      </div>

      <div className="consultant-chart">
        <div className="consultant-chart-col">
          <div className="consultant-chart-row" style={{ width: "800px" }}>
            <div className="consultant-chart-name">컨설팅 횟수 </div>
            <ReactEcharts
              option={twobar}
              style={{ height: "400px", width: "100%" }}
            />
            {/* <ReactEcharts
              option={option1}
              style={{ height: "400px", width: "100%" }}
            /> */}
          </div>
          <div className="consultant-chart-row" style={{ width: "480px" }}>
            <div className="consultant-chart-name">전문분야 분포도</div>
            <ReactEcharts
              option={chartpie}
              style={{ height: "400px", width: "100%" }}
            />
          </div>
        </div>
        <div className="consultant-chart-col">
          <div className="consultant-chart-row" style={{ width: "480px" }}>
            <div className="consultant-chart-name">평점 분포도</div>
            {/* <ReactEcharts
              option={chartbar}
              style={{ height: "400px", width: "100%" }}
            /> */}
            <ReactEcharts
              option={g3}
              style={{ height: "400px", width: "100%" }}
            />
          </div>
          <div className="consultant-chart-row" style={{ width: "800px" }}>
            <div className="consultant-chart-name">컨설팅 일 평균점수</div>
            <ReactEcharts
              option={monavg}
              style={{ height: "400px", width: "100%" }}
            />
          </div>
        </div>
      </div>
    </div>
  );
};

export default Consultant;
