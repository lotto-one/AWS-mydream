import React, { useState, useEffect } from "react";
import "./user.css";
import ReactEcharts from "echarts-for-react";
import Highcharts from "highcharts/highmaps";
import HighchartsReact from "highcharts-react-official";

import axios from "axios";

interface CapitalData {
  name: string;
  lat: number;
  lon: number;
  population: number;
  color: string;
  z?: number; // z 속성 추가
  locCd: string;
}
const categoryNameMapping: { [key: string]: string } = {
  "1": "IT",
  "2": "교육",
  "3": "마케팅",
  "4": "기획",
  "5": "경영",
};
const User = () => {
  const [totalMembers, setTotalMembers] = useState<number | null>(null); //총회원수
  const [dailyVisitors, setDailyVisitors] = useState<number | null>(null); // 일일방문자수
  const [totalIntnos, setTotalIntnos] = useState<number | null>(null); // 총 면접수
  const [totalMemCon, setTotalMemCon] = useState<number | null>(null); // 총 면접수

  const [chartData, setChartData] = useState({
    xAxisData: [] as string[], // x축 데이터
    seriesData: [] as number[], // y축 데이터
  });
  const [chartData2, setChartData2] = useState({
    xAxisData: [] as string[], // x축 데이터
    seriesData: [] as number[], // y축 데이터
  });

  const [topologyData, setTopologyData] = useState<any>(null);
  const [capitalsData, setCapitalsData] = useState<CapitalData[]>([]);

  // 총 회원수를 가져오는 함수
  const fetchTotalMembers = async () => {
    try {
      const response = await axios.get(
        `${process.env.REACT_APP_BACK_END_URL}/admin/members/count`
      );
      setTotalMembers(response.data);
    } catch (error) {
      console.error("Failed to fetch total members", error);
    }
  };

  // 일일방문자수
  const fetchDailyVisitors = async () => {
    try {
      const today = new Date().toISOString().split("T")[0]; // YYYY-MM-DD 형식으로 오늘 날짜 가져오기
      console.log("오늘", today);
      const response = await axios.get(
        `${process.env.REACT_APP_BACK_END_URL}/admin/members/daily-visit-count?date=${today}`
      );
      setDailyVisitors(response.data);
    } catch (err) {
      console.error("일일방문자수 에러", err);
    }
  };

  //총 인터뷰수
  const fetchTotalIntnos = async () => {
    try {
      const response = await axios.get(
        `${process.env.REACT_APP_BACK_END_URL}/admin/members/int-count`
      );
      setTotalIntnos(response.data);
    } catch (err) {
      console.error("총면접수 에러", err);
    }
  };

  const fetchTotalMemCon = async () => {
    try {
      const response = await axios.get(
        `${process.env.REACT_APP_BACK_END_URL}/admin/members/con-count`
      );
      setTotalMemCon(response.data);
    } catch (err) {
      console.error("총매칭수 에러", err);
    }
  };

  const [categoryChartData, setCategoryChartData] = useState<
    { value: number; name: string }[]
  >([]);

  // 1. 지도 차트 데이터
  useEffect(() => {
    const fetchMapData = async () => {
      try {
        // 한국 지도 데이터 요청
        const topologyResponse = await axios.get(
          "https://code.highcharts.com/mapdata/countries/kr/kr-all.topo.json"
        );
        setTopologyData(topologyResponse.data);

        const locCdCountsResponse = await axios.get(
          `${process.env.REACT_APP_BACK_END_URL}/admin/members/loccdCounts`
        );
        const locCdCounts = locCdCountsResponse.data;

        // 도시 데이터 설정
        const capitals: CapitalData[] = [
          {
            name: "서울",
            lat: 37.5665,
            lon: 126.978,
            locCd: "1", // 서울
            population: 9720846,
            color: "#FF0000",
          },
          {
            name: "경기도",
            lat: 37.4138,
            lon: 127.5183,
            locCd: "2", // 경기도
            population: 3413841,
            color: "#00FF00",
          },
          {
            name: "충청도",
            lat: 36.6581,
            lon: 127.648,
            locCd: "3", // 충청도
            population: 2466052,
            color: "#0000FF",
          },
          {
            name: "전라도",
            lat: 34.8679,
            lon: 126.991,
            locCd: "4", // 전라도
            population: 2947217,
            color: "#FFFF00",
          },
          {
            name: "경상도",
            lat: 35.5384,
            lon: 128.0,
            locCd: "5", // 경상도
            population: 1476974,
            color: "#00FFFF",
          },
          {
            name: "강원도",
            lat: 37.8228,
            lon: 128.1555,
            locCd: "6", // 강원도
            population: 1486849,
            color: "#FF00FF",
          },
          {
            name: "제주도",
            lat: 33.4996,
            lon: 126.5312,
            locCd: "7", // 제주도
            population: 675883,
            color: "#008000",
          },
        ];

        // locCdCounts 데이터를 각 도시에 매핑
        capitals.forEach((capital) => {
          const locCd = capital.locCd;
          const count = locCdCounts[locCd];
          if (count) {
            capital.population = count; // 또는 원하는 대로 데이터를 추가하거나 변경
            capital.z = count; // 예시: z 값에 인구 대신 locCdCounts의 값을 설정
          }
        });

        setCapitalsData(capitals);
      } catch (error) {
        console.error("Error fetching map data:", error);
      }
    };

    fetchMapData();
  }, []);

  // 2. 일별 회원 수 데이터를 가져오는 useEffect
  useEffect(() => {
    fetchTotalMembers();
    fetchDailyVisitors();
    fetchTotalIntnos();
    fetchTotalMemCon();
    const fetchDailyCounts = async () => {
      try {
        const endDate = new Date();

        // 일주일 전 날짜 구하기
        const startDate = new Date();
        startDate.setDate(endDate.getDate() - 7);

        // 날짜 포맷팅 함수
        const formatDate = (date: Date) => {
          const year = date.getFullYear();
          const month = String(date.getMonth() + 1).padStart(2, "0");
          const day = String(date.getDate()).padStart(2, "0");
          return `${year}-${month}-${day}`;
        };

        const formattedStartDate = formatDate(startDate);
        const formattedEndDate = formatDate(endDate);

        // 일별 회원 수 데이터 요청
        const dailyCountsResponse = await axios.get(
          `${process.env.REACT_APP_BACK_END_URL}/admin/members/dailyCounts`,
          {
            params: {
              startDate: formattedStartDate,
              endDate: formattedEndDate,
            },
          }
        );

        const data = dailyCountsResponse.data;
        const labels = Object.keys(data);
        const values = Object.values(data);

        // 차트 데이터 설정
        setChartData({
          xAxisData: labels.map((date) => `${date.split("월")[1]}`), // x축 데이터
          seriesData: values as number[], // y축 데이터
        });
      } catch (error) {
        console.error("Error fetching daily counts:", error);
      }
    };

    fetchDailyCounts();
  }, []);

  // 카테고리 데이터 가져오기
  useEffect(() => {
    const fetchCategoryData = async () => {
      try {
        const response = await axios.get(
          `${process.env.REACT_APP_BACK_END_URL}/admin/members/categcdCounts`
        );
        const data = response.data;

        // 데이터를 카테고리 차트 형식에 맞게 변환
        const formattedCategoryData = Object.keys(data).map((key) => ({
          value: data[key],
          name: categoryNameMapping[key] || `Category ${key}`, // 카테고리 이름 설정
        }));

        setCategoryChartData(formattedCategoryData); // 카테고리 차트 데이터 설정
      } catch (error) {
        console.error("Error fetching category data:", error);
      }
    };

    fetchCategoryData();
  }, []);

  useEffect(() => {
    const fetchDailyInterview = async () => {
      try {
        const endDate = new Date();
        const startDate = new Date();
        startDate.setDate(endDate.getDate() - 7);
        const formatDate = (date: Date) => {
          const year = date.getFullYear();
          const month = String(date.getMonth() + 1).padStart(2, "0");
          const day = String(date.getDate()).padStart(2, "0");
          return `${year}-${month}-${day}`;
        };
        const formattedStartDate = formatDate(startDate);
        const formattedEndDate = formatDate(endDate);
        const dailyintresponse = await axios.get(
          `${process.env.REACT_APP_BACK_END_URL}/admin/members/daily-interview`,
          {
            params: {
              startDate: formattedStartDate,
              endDate: formattedEndDate,
            },
          }
        );
        const data = dailyintresponse.data;
        const labels = Object.keys(data);
        const values = Object.values(data);

        // 차트 데이터 설정
        setChartData2({
          xAxisData: labels.map((date) => `${date.split("월")[1]}`), // x축 데이터
          seriesData: values as number[], // y축 데이터
        });
      } catch (error) {
        console.error("컨설팅면접수에러", error);
      }
    };

    fetchDailyInterview();
  }, []);
  /* 지도차트 */
  const chartOptions = {
    //지도차트
    title: {
      text: "", // 제목
      style: {
        fontFamily: "Jua",
      },
    },
    series: [
      {
        name: "Basemap",
        mapData: topologyData,
        accessibility: {
          exposeAsGroupOnly: true,
        },
        borderColor: "#606060", // 지도 선색
        nullColor: "rgba(200, 200, 200, 0.1)", // 지도색
        showInLegend: false,
      },
      {
        type: "mapbubble",
        dataLabels: {
          enabled: true,
          format: "{point.capital}",
          style: {
            color: "white", // 텍스트 색상을 기본값으로 설정
            textOutline: "none", // 텍스트 외곽선을 없앰
          },
        },
        accessibility: {
          point: {
            valueDescriptionFormat:
              "하하, " +
              "{point.parentState}. Population {point.population}. " +
              "Latitude {point.lat:.2f}, longitude {point.lon:.2f}.",
          },
        },
        name: "",
        data: capitalsData,
        maxSize: "10%",
        color: Highcharts.getOptions().colors,
        tooltip: {
          pointFormat: "{point.name}<br>유저수: {point.population}",
        },
      },
    ],
  };
  /* 유저수 차트 */
  const option1 = {
    xAxis: {
      type: "category",
      boundaryGap: false,
      data: chartData.xAxisData,
    },
    yAxis: {
      type: "value",
    },
    tooltip: {
      trigger: "axis", // 'axis'로 설정하여 x축과 y축의 데이터를 기준으로 툴팁 표시
      // 기본 툴팁 포맷을 사용하여 마우스 오버 시 자동으로 표시됩니다.
    },
    series: [
      {
        data: chartData.seriesData,
        type: "line",
        areaStyle: {},
      },
    ],
  };
  /* 카테고리차트 */
  const chartpie = {
    tooltip: {
      trigger: "item",
      formatter: "{b}: {c}", // {b}는 항목 이름, {c}는 값
      backgroundColor: "rgba(50, 50, 50, 0.7)", // 툴팁 배경색
      textStyle: {
        color: "#fff", // 툴팁 텍스트 색상
      },
    },
    series: [
      {
        name: "Category Distribution",
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
        data: categoryChartData, // API에서 받아온 카테고리 데이터
      },
    ],
  };
  /* 주간컨설팅면접 */
  const barchartOption = {
    xAxis: {
      type: "category",
      data: chartData2.xAxisData, // x축 데이터
    },
    yAxis: {
      type: "value",
    },
    tooltip: {
      trigger: "axis", // axis의 데이터를 기준으로 툴팁 표시
      // formatter를 생략하면 기본적으로 값이 표시됩니다.
    },
    series: [
      {
        data: chartData2.seriesData, // y축 데이터
        type: "bar",
      },
    ],
  };

  // let base = +new Date(1988, 9, 3);
  // let oneDay = 24 * 3600 * 1000;
  // let data = [[base, Math.random() * 300]];
  // for (let i = 1; i < 20000; i++) {
  //   let now = new Date((base += oneDay));
  //   data.push([+now, Math.round((Math.random() - 0.5) * 20 + data[i - 1][1])]);
  // }
  return (
    <div className="user-container">
      <div className="user-header">
        <div>
          <p className="user-text">&nbsp;User DashBoard</p>
        </div>
      </div>
      <br></br>
      <div className="user-dash">
        <div className="user-info1">
          <div>
            <h3 className="user-info-text">누적 회원수</h3>
            <div>
              <p className="user-info-textinfo">
                {totalMembers !== null
                  ? `${totalMembers.toLocaleString()}명`
                  : "로딩 중..."}
              </p>
            </div>
          </div>
          <div className="user-info2">
            <img src="/img/admin_img/people.svg" className="user-svg"></img>
          </div>
        </div>
        <div className="user-info1">
          <div>
            <h3 className="user-info-text">일일 방문자</h3>
            <div>
              <p className="user-info-textinfo">
                {dailyVisitors !== null
                  ? `${dailyVisitors.toLocaleString()}명`
                  : "로딩 중..."}
              </p>
            </div>
          </div>
          <div className="user-info2">
            <img src="/img/admin_img/person.svg" className="user-svg"></img>
          </div>
        </div>
        <div className="user-info1">
          <div>
            <h3 className="user-info-text">AI면접 횟수</h3>
            <div>
              <p className="user-info-textinfo">
                {totalIntnos !== null
                  ? `${totalIntnos.toLocaleString()}번`
                  : "로딩 중..."}
              </p>
            </div>
          </div>
          <div className="user-info2">
            <img src="/img/admin_img/dollar.svg" className="user-svg"></img>
          </div>
        </div>
        <div className="user-info1">
          <div>
            <h3 className="user-info-text">컨설팅 신청횟수</h3>
            <div>
              <p className="user-info-textinfo">
                {totalMemCon !== null
                  ? `${totalMemCon.toLocaleString()}번`
                  : "로딩 중..."}
              </p>
            </div>
          </div>
          <div className="user-info2">
            <img src="/img/admin_img/dollars.svg" className="user-svg"></img>
          </div>
        </div>
      </div>

      <div className="user-chart">
        <div className="user-chart-col">
          <div className="user-chart-row" style={{ width: "800px" }}>
            <div className="chart-name">주간 유저 증가량</div>
            <ReactEcharts
              option={option1}
              style={{ height: "400px", width: "100%" }}
            />
          </div>
          <div className="user-chart-row" style={{ width: "480px" }}>
            <div className="chart-name">희망 직군</div>
            <ReactEcharts
              option={chartpie}
              style={{ height: "400px", width: "100%" }}
            />
          </div>
        </div>
        <div className="user-chart-col">
          <div className="user-chart-row" style={{ width: "480px" }}>
            <div className="chart-name">지역별 이용도</div>
            <div>
              {topologyData && (
                <HighchartsReact
                  highcharts={Highcharts}
                  constructorType={"mapChart"}
                  options={chartOptions}
                />
              )}
            </div>
          </div>
          <div className="user-chart-row" style={{ width: "800px" }}>
            <div className="chart-name">주간 컨설팅횟수</div>
            <ReactEcharts
              option={barchartOption}
              style={{ height: "400px", width: "100%" }}
            />
          </div>
        </div>
      </div>
    </div>
  );
};

export default User;
