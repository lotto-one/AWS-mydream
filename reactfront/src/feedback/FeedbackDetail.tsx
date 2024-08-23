import React, { useEffect, useState } from 'react';
import axios from 'axios';
import * as echarts from 'echarts';
import Highcharts from 'highcharts';
import HighchartsWordcloud from 'highcharts/modules/wordcloud';
import 'echarts/lib/chart/radar';
import 'echarts/lib/component/tooltip';
import 'echarts/lib/component/title';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import './FeedbackDetail.css';

HighchartsWordcloud(Highcharts);

interface QnaData {
  question: string;
  answer: string;
  aifeedbk: string;
  QCNSFEEDBK: string;
}

interface FeedbackDetailParams {
  intno: number;
}

interface QconEvalItem {
  qcnsfeedbk: string;

}
interface ApiResponse {
  QconEval: QconEvalItem[];
}

const FeedbackDetail: React.FC = () => {
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const intno = searchParams.get('intno');
  const navigate = useNavigate();
  const [activePage, setActivePage] = useState(1);
  const [page, setPage] = useState(1);
  const [question, setQuestion] = useState<string>('');
  const [answer, setAnswer] = useState<string>('');
  const [feedback, setFeedback] = useState<string>('');
  const [userName, setUserName] = useState<string>('');
  const [credt, setCredt] = useState<string>('');
  const [pbadcnt, setPbadcnt] = useState<number[]>([]);
  const [qnaData, setQnaData] = useState<QnaData[]>([]);
  const [cnsfeedbk, setCnsfeedbk] = useState<string>('');
  const [CTFeedback, setCTFeedback] = useState<string[]>([]);
  const [efeed1, setEfeed1] = useState<string>('');
  const [efeed2, setEfeed2] = useState<string>('');
  const [pfeed1, setPfeed1] = useState<string>('');
  const [pfeed2, setPfeed2] = useState<string>('');
  const [sttfeed1, setSttfeed1] = useState<string>('');
  const [sttfeed2, setSttfeed2] = useState<string>('');
  const [vfeed1, setVfeed1] = useState<string>('');
  const [vfeed2, setVfeed2] = useState<string>('');
  const i = page - 1;

  const handleFeedbackChange = (event: React.ChangeEvent<HTMLTextAreaElement>) => {
    const newFeedback = [event.target.value];
    setCTFeedback(newFeedback);
  }

  const totalevalChange = () => {
    const textareaElement = document.getElementById('totaleval') as HTMLTextAreaElement;
    if (textareaElement) {
      const newCnsfeedbk = textareaElement.value;
      setCnsfeedbk(newCnsfeedbk);
    }
  }

  const CTFeedSubmit = () => {
    console.log("보낼거", CTFeedback, intno, page);
    const qcnsfeedbk = CTFeedback[0].toString();
    console.log("내용:",qcnsfeedbk);
    console.log(typeof(intno), typeof(page))
    axios
      .post(`${process.env.REACT_APP_BACK_END_URL}/consultant/feedback/feedbkQUpdate`, { "qcnsfeedbk": qcnsfeedbk, "intno": intno, "qno": page })
      .then((response) => {
        console.log("업데이트 성공");
        window.location.reload();
      })
      .catch((error) => {
        console.error("Error submitting feedback:", error);
      });
  };

  const CTCnsFeedSubmit = () => {
    console.log("보낼거", cnsfeedbk, intno, typeof(cnsfeedbk), typeof(intno));
    axios
      .post(`${process.env.REACT_APP_BACK_END_URL}/consultant/feedback/feedbkCnsUpdate`, { "cnsfeedbk": cnsfeedbk, "intno": intno })
      .then((response) => {
        console.log("업데이트 성공");
        window.location.reload();
      })
      .catch((error) => {
        console.error("Error submitting feedback:", error);
      });
  };

  useEffect(() => {
    fetchUserData();
    fetchQuestionData();
    fetchCTFeedback();
    initializeCharts();
    fetchTotalFeedbk();
    fetchTotalConFeedbk();
  }, []);
  

  const fetchUserData = async () => {
    try {
      console.log(intno);
      const response = await axios.get(`${process.env.REACT_APP_BACK_END_URL}/consultant/feedback/feedbkDetailInfo`, { params: { intno } });
      console.log(response.data);
      setUserName(response.data.info.name);
      setCredt(response.data.info.upddt);


    } catch (error) {
      console.error('서버 오류:', error);
    }
  };

  const formatDate = (datetime: string) => {
    return datetime.split('T')[0];
  };

  const fetchQuestionData = async () => {
    try {
      const response = await axios.get(`${process.env.REACT_APP_BACK_END_URL}/consultant/feedback/feedbkScore`, { params: { intno } });
      setQnaData(response.data.escore);
      console.log("여기",response.data.escore)
      if (response.data.escore[0]) {
        setQuestion(response.data.escore[0].question);
        setAnswer(response.data.escore[0].answer);
        setFeedback(response.data.escore[0].aifeedbk);
      }
    } catch (error) {
      console.error('서버 오류:', error);
    }
  };

  const fetchCTFeedback = async () => {
    try {
      const response = await axios.get<ApiResponse>(`${process.env.REACT_APP_BACK_END_URL}/consultant/feedback/feedbkDetailQconEval`, { params: { intno } });
      console.log("요고", response.data.QconEval);
      const qcnsfeedbkArray = response.data.QconEval.map(item => item.qcnsfeedbk);
      console.log("지금:", qcnsfeedbkArray);
      setCTFeedback(qcnsfeedbkArray);
    } catch (error) {
      console.error('서버 오류:', error);
    }
  };

  const next = () => {
    if (page < 7) {
      pageChange(page + 1);
    }
  };

  const previous = () => {
    if (page > 1) {
      pageChange(page - 1);
    }
  };

  const handleClose = () => {
    navigate('/consultant/feedback/main');
  };

  const pageChange = (num: number) => {
    setPage(num);
    setActivePage(num);

    const currentData = qnaData[num-1];
    console.log("현재", currentData);
    if (currentData) {
      setQuestion(currentData.question);
      setAnswer(currentData.answer);
      setFeedback(currentData.aifeedbk);
    }
  };

  const initializeCharts = () => {
    face();
    wordcloud();
    barchart();
    voiceg();
  };

  const fetchTotalFeedbk = async () => {
    const response = await axios.get(`${process.env.REACT_APP_BACK_END_URL}/consultant/feedback/feedbkDetailResEval`, {params : {intno}});
    console.log(response.data);
    try {
      setEfeed1(response.data.resEval.efeed1);
    setEfeed2(response.data.resEval.efeed2);
    setPfeed1(response.data.resEval.pfeed1);
    setPfeed2(response.data.resEval.pfeed2);
    setVfeed1(response.data.resEval.vfeed1);
    setVfeed2(response.data.resEval.vfeed2);
    setSttfeed1(response.data.resEval.sttfeed1);
    setSttfeed2(response.data.resEval.sttfeed2);
    } catch (error) {
      console.error('값 없음:', error);
    }
  }

  const fetchTotalConFeedbk = async () => {
    const response = await axios.get(`${process.env.REACT_APP_BACK_END_URL}/consultant/feedback/feedbkDetailResConEval`, {params : {intno}});
    console.log(response.data);
    setCnsfeedbk(response.data.resConEval.cnsfeedbk);
  }

  const face = async () => {
    const chartContainer = document.getElementById('face');
    if (chartContainer) {
      chartContainer.style.width = '300px';
      chartContainer.style.height = '400px';

      const myChart = echarts.init(chartContainer);

      try {
        const response = await axios.get(`${process.env.REACT_APP_BACK_END_URL}/consultant/feedback/feedbkScore`, { params: { intno } });
        console.log(response.data);
        const option = {
          title: {
            text: '감정 점수'
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            left: 'left',
            data: ['점']
          },
          radar: [
            {
              indicator: [
                { text: 'Q1', max: 100 },
                { text: 'Q2', max: 100 },
                { text: 'Q3', max: 100 },
                { text: 'Q4', max: 100 },
                { text: 'Q5', max: 100 },
                { text: 'Q6', max: 100 },
                { text: 'Q7', max: 100 }
              ],
              center: ['50%', '50%'],
              radius: 80
            }
          ],
          series: [
            {
              type: 'radar',
              tooltip: {
                trigger: 'item'
              },
              areaStyle: {
                color: '#08AD94'
              },
              itemStyle: {
                color: '#0A6065'
              },
              data: [
                {
                  value: [response.data.escore[0].escore, 
                          response.data.escore[1].escore, 
                          response.data.escore[2].escore, 
                          response.data.escore[3].escore, 
                          response.data.escore[4].escore, 
                          response.data.escore[5].escore,
                          response.data.escore[6].escore],
                  name: '감정 점수'
                }
              ]
            }
          ]
        };
        myChart.setOption(option);
      } catch (error) {
        console.error('서버 오류:', error);
      }
    }
  };

  const barchart = async () => {
    try {
      const response = await axios.get(`${process.env.REACT_APP_BACK_END_URL}/consultant/feedback/feedbkScore`, { params: { intno } });
      Highcharts.chart('barchart', {
        chart: {
          type: 'column',  // 차트 유형을 column으로 설정
          height: '380px'
        },
        title: {
          text: '자세 흐트러짐',
          align: 'left'
        },
        xAxis: {
          categories: ['Q1', 'Q2', 'Q3', 'Q4', 'Q5', 'Q6', 'Q7'],
          crosshair: true
        },
        yAxis: {
          min: 0,
          title: {
            text: 'Count'
          }
        },
        tooltip: {
          valueSuffix: '(회)'
        },
        plotOptions: {
          column: {
            pointPadding: 0.2,
            borderWidth: 0
          }
        },
        series: [
          {
            type: 'column', // 각 series에 차트 유형을 명시
            name: response.data.chartname,
            data: [response.data.escore[0].pscore, 
                  response.data.escore[1].pscore, 
                  response.data.escore[2].pscore, 
                  response.data.escore[3].pscore, 
                  response.data.escore[4].pscore, 
                  response.data.escore[5].pscore,
                  response.data.escore[6].pscore],
            color: '#08AD94'
          }
        ]
      });
    } catch (error) {
      console.error('서버 오류:', error);
    }
  };

const voiceg = async () => {
    try {
  const response = await axios.get(`${process.env.REACT_APP_BACK_END_URL}/consultant/feedback/feedbkScore`, { params: { intno } });

  Highcharts.chart('voiceg', {
    chart: {
      height: 380,
      width: 470,
      type: 'line'
    },
    title: {
      text: '음성 데이터',
      align: 'left',
    },
    subtitle: {
      text: '',
      align: 'left',
    },
    yAxis: {
      title: {
        text: '',
      },
    },
    xAxis: {
      title: {
        text: '(문제)',
      },
      accessibility: {
        rangeDescription: '(문제)',
      },
      tickPositions: [0, 1, 2, 3, 4, 5, 6, 7],
    },
    legend: {
      layout: 'vertical',
      align: 'right',
      verticalAlign: 'middle',
    },
    plotOptions: {
      series: {
        label: {
          connectorAllowed: false,
        },
        pointStart: 1,
        pointInterval: 1,
      },
    },
    series: [
      {
        name: 'vhertz',
        type: 'line',
        data: 
        [response.data.escore[0].vhertz, 
        response.data.escore[1].vhertz, 
        response.data.escore[2].vhertz, 
        response.data.escore[3].vhertz, 
        response.data.escore[4].vhertz, 
        response.data.escore[5].vhertz,
        response.data.escore[6].vhertz],
      },
      {
        name: 'vjitter',
        type: 'line',
        data: 
        [response.data.escore[0].vjitter, 
        response.data.escore[1].vjitter, 
        response.data.escore[2].vjitter, 
        response.data.escore[3].vjitter, 
        response.data.escore[4].vjitter, 
        response.data.escore[5].vjitter,
        response.data.escore[6].vjitter],
      },
      {
        name: 'vspeed',
        type: 'line',
        data: 
        [response.data.escore[0].vspeed, 
        response.data.escore[1].vspeed, 
        response.data.escore[2].vspeed, 
        response.data.escore[3].vspeed, 
        response.data.escore[4].vspeed, 
        response.data.escore[5].vspeed,
        response.data.escore[6].vspeed],
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
              layout: 'horizontal',
              align: 'center',
              verticalAlign: 'bottom',
            },
          },
        },
      ],
    },
  });
    } catch (error) {
      console.error('서버 오류:', error);
    }
  };

  const wordcloud = async () => {
    try {
      const response = await axios.get(`${process.env.REACT_APP_BACK_END_URL}/consultant/feedback/feedbkScore`, { params: { intno } });
      console.log(response.data.escore);
      const text: string = response.data.escore[0].answer + 
                           response.data.escore[1].answer +
                           response.data.escore[2].answer +
                           response.data.escore[3].answer +
                           response.data.escore[4].answer +
                           response.data.escore[5].answer +
                           response.data.escore[6].answer;
      // console.log(text);
      const lines: string[] = text.replace(/[():'?0-9]+/g, '').split(/[,\. ]+/g);
      const data = lines.reduce((arr: { name: string; weight: number }[], word: string) => {
        let obj = arr.find(obj => obj.name === word);
        if (obj) {
          obj.weight += 1;
        } else {
          obj = {
            name: word,
            weight: 1
          };
          arr.push(obj);
        }
        return arr;
      }, []);

      const top100 = data.sort((a, b) => b.weight - a.weight).slice(0, 100);

      Highcharts.chart('wordcloud', {
        series: [
          {
            type: 'wordcloud',
            data: top100,
            name: 'Occurrences'
          }
        ],
        title: {
          text: '자주 언급된 단어'
        }
      });
    } catch (error) {
      console.error('서버 오류:', error);
    }
  };

  return (
    <div className="container">
        <div className="feedback-con row">
          {/* Summary Section */}
          <div className="feedback-summary">
            <div className="feedback-total-t row">
              <div className="feedback-total-title col-1">종합 분석</div>
              <div className="feedback-total-subtitle col-1">- 직무 면접</div>
            </div>
            <div className="feedback-summary-right">
              <p>이름: {userName}</p>
              <p>날짜: {formatDate(credt)}</p>
            </div>
          </div>

          <div className="feedback-subtitle3 col-1">
            <div className="feedback-subtitlecon3">종합 평가</div>
          </div>

          {/* Analysis Section */}
          <div className="feedback-totalcom">
              <div className='feedback-analyze6-con'>
              <li> { efeed1 }</li>
              <li> { efeed2 }</li>
              <li> { pfeed1 }</li>
              <li> { pfeed2 }</li>
              <li> { vfeed1 }</li>
              <li> { vfeed2 }</li>
              <li> { sttfeed1 }</li>
              <li> { sttfeed2 }</li>
              </div>
          </div>

          <div className="feedback-subtitle3 col-1">
            <div className="feedback-subtitlecon3">컨설턴트 평가</div>
          </div>

          <div className="feedback-totalcom">
            <textarea className="feedback-textarea-ct" id='totaleval' value={cnsfeedbk}
              onChange={totalevalChange}
              />
            <button className='feedback-submit col-1' onClick={CTCnsFeedSubmit}>작성하기</button>
          </div>

          <div className="feedback-subtitle3 col-1">
            <div className="feedback-subtitlecon3">표정 분석 / 자세 분석</div>
          </div>

          {/* Charts and Data Section */}
          <div className='feedback-qcon5'>
            <div id="face" className="feedback-chart-container1"></div>
            <div id="barchart" className="feedback-chart-container2"></div>
          </div>

          <div className="feedback-subtitle3 col-1">
            <div className="feedback-subtitlecon3">유사도 측정 / 음성 분석</div>
          </div>

          <div className='feedback-qcon8'>
            <div id="wordcloud" className="feedback-chart-container4"></div>
            <div id="voiceg" className="feedback-chart-container3"></div>
          </div>

          

          <div className="feedback-qcon9">
            <div className="feedback-similartip">
              <img className="feedback-tipicon" src="img/res_tip.png" />
              <div className="feedback-ti">회원님이 작성한 이력서의 기반으로 키워드를 추출하여 면접 내용과 비교하여 유사도를 ...</div>
            </div>
          </div>

          <div className="feedback-btncon row">
            {[1, 2, 3, 4, 5, 6, 7].map(page => (
              <div
                key={page}
                onClick={() => pageChange(page)}
                className={`col-1 ${activePage === page ? 'feedback-active' : 'feedback-button'}`}
              >
                Q{page}
              </div>
            ))}
          </div>
          <div className="row scrollable-div">
          <div className="feedback-qcon">
            <div className="feedback-q">
              <div className="feedback-qnum">Q{i + 1}.</div>
              <div className="feedback-question">{question}</div>
            </div>
          </div>

          <div className="feedback-qcontent1">
            <div className="feedback-subtitle1 row">
              <div className="feedback-subtitle2 col-1">면접 대답</div>
              <div className="feedback-qcontent2">{answer}</div>
            </div>
          </div>

            <div className="feedback-qcon4">
              <div className="feedback-qcontent1">
                <div className="feedback-subtitle1 row">
                  <div className="feedback-subtitle2 col-1">AI피드백</div>
                  <div className="feedback-qcontent2">{feedback}</div>
                </div>
              </div>
            </div>
          

          {((i === 5 || i === 6)) && (
            <div className="feedback-qcontent1">
              <div className="feedback-subtitle1 row">
                <div className="feedback-subtitle2 col-1">컨설턴트 피드백</div>
                  <div className="feedback-qcontent2">
                    <textarea className="feedback-textarea" value={CTFeedback[i - 5]}
                    onChange={handleFeedbackChange}
                    />
                    <button className='feedback-submit col-1' onClick={CTFeedSubmit}>작성하기</button>
                  </div>
              </div>
            </div>
          )}
       
        </div>
        {/* Navigation */}
        <div className="reshdudy-btnarea">
      {page > 1 && (
        <button className="feedback-nextbtn col-1" onClick={previous}>
          이전 문항
        </button>
      )}
      {page < 7 && (
        <button className="feedback-nextbtn col-1" onClick={next}>
          다음 문항
        </button>
      )}
      <button className="feedback-homebtn col-1" onClick={handleClose}>
        돌아가기
      </button>
    </div>
      </div>
    </div>
  );
};

export default FeedbackDetail;
