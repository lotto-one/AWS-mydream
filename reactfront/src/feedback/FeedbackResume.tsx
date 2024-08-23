import React, { useEffect, useState } from 'react'
import './FeedbackResume.css'
import axios from 'axios';

interface BasicInfo {
    rsmno: number;
    title: string;
    name: string;
    birthymd: string;
    addr: string;
    mphonenum: string;
    hphonenum: string;
    email: string;
    imgname: string;
  }

  interface Education {
    entymd: string;
    gradeymd: string;
    schoolname: string;
    major: string;
    gradeuateyn: string;
    gradescore: string;
    seqno: number;
  }

  interface Career {
    entymd: string;
    quitymd: string;
    compname: string;
    jobclass: string;
    maintask: string;
    jobposition: string;
    seqno: number;
  }

  interface Introduction {
    title: string;
    content: string;
    seqno: number;
  }

const FeedbackResume: React.FC = () => {
    const [memno,setMemno] = useState<number>(1);
    const [rsmno,setRsmno] = useState<number>();
    const [basic, setBasic] = useState<BasicInfo | null>(null);
    const [edu, setEdu] = useState<Education[]>([]);
    const [car, setCar] = useState<Career[]>([]);
    const [intro, setIntro] = useState<Introduction[]>([]);


    useEffect(() => {
        const memno = new URLSearchParams(window.location.search).get('memno');
        if (memno) {
            setMemno(parseInt(memno));
            console.log("memno", memno, typeof(memno));
            fetchData(parseInt(memno));
        } else {
          console.log('이력서 작성');
        }
      }, []);
    
    const fetchData = async (memno: number) => {
        console.log("보내는 memno", memno, typeof(memno));
        const response = await axios.get(`${process.env.REACT_APP_BACK_END_URL}/consultant/feedback/resumeInfo`, { params: { memno } })
        console.log(response.data, typeof(response.data));
        const basicInfo = response.data;

        setBasic(basicInfo);
        console.log("확인",basicInfo.rsmno);
        setRsmno(basicInfo.rsmno);

        const rsmno = basicInfo.rsmno;
        console.log("rsmno", rsmno);
        const responseEdu = await axios.post(`${process.env.REACT_APP_BACK_END_URL}/consultant/feedback/resumeEdu`, {"rsmno": rsmno})
        console.log(responseEdu.data, typeof(response.data));
        const education = responseEdu.data;
        setEdu(education);

        const responseCar = await axios.post(`${process.env.REACT_APP_BACK_END_URL}/consultant/feedback/resumeCar`, {"rsmno": rsmno})
        console.log(responseCar.data, typeof(response.data));
        const career = responseCar.data;
        setCar(career);

        const responseSelf = await axios.post(`${process.env.REACT_APP_BACK_END_URL}/consultant/feedback/resumeSelf`, {"rsmno": rsmno})
        console.log(responseSelf.data, typeof(response.data));
        const self = responseSelf.data;
        setIntro(self);
    };

    return (
        <div className="container">
          <div className="cv-container">
            <div className="CVtitle">
              <blockquote className="blockquote-CV">
                <b>
                  <p>{basic?.name} 님 이력서</p>
                </b>
              </blockquote>
            </div>

            <div className="form-container">
              <table className="cvwrite-cv-table">
                <tbody>
                  <tr>
                    <td className="cvwrite-cv-td1">이력서 제목</td>
                    <td className="cvwrite-cv-td2">
                      <input
                        className="cvwrite-cv-input"
                        value={basic?.title}
                        readOnly
                      />
                    </td>
                  </tr>
                </tbody>
              </table>
              <div className="cvwrite-cv1">
                <div className="cvwrite-cv1-title">인적사항</div>
                <table className="cvwrite-cv1-table">
                  <tbody>
                    <tr className="cvwrite-cv1-tr">
                      <td rowSpan={4} className="cvwrite-cv1-td1">
                        <img src={basic?.imgname}
                          alt='default.png'
                          id="inputImg"
                          style={{ display: 'none' }}
                        />
                      </td>
                      <td className="cvwrite-cv1-td2">성명</td>
                      <td>
                        <input
                          type="text"
                          className="cvwrite-cv1-input"
                          placeholder="성명"
                          value={basic?.name}
                          readOnly
                        />
                      </td>
                      <td className="cvwrite-cv1-td4">출생년도</td>
                      <td>
                        <input
                          type="text"
                          className="cvwrite-cv1-input"
                          placeholder="YYYYMMDD"
                          value={basic?.birthymd || ''}
                          readOnly
                          maxLength={8}
                        />
                      </td>
                    </tr>
                    <tr className="cvwrite-cv1-tr">
                      <td className="cvwrite-cv1-td2">주소</td>
                      <td colSpan={3}>
                        <input
                          type="text"
                          className="cvwrite-cv1-input"
                          value={basic?.addr || ''}
                          readOnly
                        />
                      </td>
                    </tr>
                    <tr className="cvwrite-cv1-tr">
                      <td className="cvwrite-cv1-td2">일반 전화</td>
                      <td>
                        <input
                          type="text"
                          className="cvwrite-cv1-input"
                          value={basic?.mphonenum || ''}
                          readOnly
                        />
                      </td>
                      <td className="cvwrite-cv1-td4">휴대 전화</td>
                      <td>
                        <input
                          type="text"
                          className="cvwrite-cv1-input"
                          value={basic?.hphonenum || ''}
                          readOnly
                        />
                      </td>
                    </tr>
                    <tr className="cvwrite-cv1-tr">
                      <td className="cvwrite-cv1-td2">이메일</td>
                      <td colSpan={3}>
                        <input
                          type="text"
                          className="cvwrite-cv1-input"
                          value={basic?.email || ''}
                          readOnly
                        />
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
              <div className="cvwrite-cv2">
                <div className="cvwrite-cv1-title">학력</div>
                <table className="cvwrite-cv1-table">
                  <thead>
                    <tr>
                      <th>입학년도</th>
                      <th>졸업년도</th>
                      <th>학교명</th>
                      <th>전공</th>
                      <th>졸업여부</th>
                      <th>성적</th>
                    </tr>
                  </thead>
                  <tbody>
                    {edu.map((e, index) => (
                      <tr key={index}>
                        <td>
                          <input
                            type="text"
                            className="cvwrite-cv1-input"
                            value={e.entymd}
                            readOnly
                          />
                        </td>
                        <td>
                          <input
                            type="text"
                            className="cvwrite-cv1-input"
                            value={e.gradeymd}
                            readOnly
                          />
                        </td>
                        <td>
                          <input
                            type="text"
                            className="cvwrite-cv1-input"
                            value={e.schoolname}
                            readOnly
                          />
                        </td>
                        <td>
                          <input
                            type="text"
                            className="cvwrite-cv1-input"
                            value={e.major}
                            readOnly
                          />
                        </td>
                        <td>
                          <input
                            type="text"
                            className="cvwrite-cv1-input"
                            value={e.gradeuateyn}
                            readOnly
                          />
                        </td>
                        <td>
                          <input
                            type="text"
                            className="cvwrite-cv1-input"
                            value={e.gradescore}
                            readOnly
                          />
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
              <div className="cvwrite-cv3">
                <div className="cvwrite-cv1-title">경력</div>
                <table className="cvwrite-cv1-table">
                  <thead>
                    <tr>
                      <th>입사년도</th>
                      <th>퇴사년도</th>
                      <th>회사명</th>
                      <th>직무</th>
                      <th>업무내용</th>
                      <th>직급</th>
                    </tr>
                  </thead>
                  <tbody>
                    {car.map((c, index) => (
                      <tr key={index}>
                        <td>
                          <input
                            type="text"
                            className="cvwrite-cv1-input"
                            value={c.entymd}
                            readOnly
                          />
                        </td>
                        <td>
                          <input
                            type="text"
                            className="cvwrite-cv1-input"
                            value={c.quitymd}
                            readOnly
                          />
                        </td>
                        <td>
                          <input
                            type="text"
                            className="cvwrite-cv1-input"
                            value={c.compname}
                            readOnly
                          />
                        </td>
                        <td>
                          <input
                            type="text"
                            className="cvwrite-cv1-input"
                            value={c.jobclass}
                            readOnly
                          />
                        </td>
                        <td>
                          <input
                            type="text"
                            className="cvwrite-cv1-input"
                            value={c.maintask}
                            readOnly
                          />
                        </td>
                        <td>
                          <input
                            type="text"
                            className="cvwrite-cv1-input"
                            value={c.jobposition}
                            readOnly
                          />
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
              <div className="cvwrite-cv4">
                <div className="cvwrite-cv1-title">자기소개</div>
                <table className="cvwrite-cv4-table">
                    <tr className="cvwrite-cv4-tr">
                        <th className="cvwrite-cv4-th1">질문</th>
                        <th className="cvwrite-cv4-th2">내용</th>
                    </tr>
                    {intro.map((i, index) => (
                    <tr key={index}>
                        <td className="cvwrite-cv4-td1">
                            <textarea
                            className="auto-resize"
                            value={i.title}
                            />
                        </td>
                        <td className="cvwrite-cv4-td2">
                            <textarea
                            className="auto-resize"
                            value={i.content}
                            readOnly
                            />
                        </td>
                    </tr>
                    ))}
                </table>
              </div>
            </div>
          </div>
        </div>
      );
    };
    
    export default FeedbackResume;