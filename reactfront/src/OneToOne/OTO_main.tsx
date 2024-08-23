import React, { useState, useEffect, useRef } from "react";
import { Link } from "react-router-dom";
import "./OTO_main.css";
import axios from "axios";

interface Item {
  name: string;
  chtno: string;
  imgname: string;
}

const OTO_main: React.FC = () => {
  const [cnsno] = useState(localStorage.getItem("cnsno"));
  const [rows, setRows] = useState<Item[][]>([]);
  const items: Item[][] = [
    // {chtno: 43, nickname: '홍길동닉네임4', imgname: 'default.png'} 예시 데이터
  ];

  const chatlist = async () => {
    try {
      const res = await axios.get(
        `${process.env.REACT_APP_BACK_END_URL}/consultant/chat/getlist?cnsno=${cnsno}`,
        {
          headers: { "Content-Type": "application/json" },
        }
      );
      //items=res.data
      items.push(res.data);
      console.log(res.data);
      console.log(items);
      console.log(items[0]);
      const rows = items
        .flatMap((itemList) => itemList)
        .reduce<Item[][]>((acc, item, index) => {
          if (index % 3 === 0) acc.push([]);
          acc[Math.floor(index / 3)].push(item);
          return acc;
        }, []);

      setRows(rows);
    } catch (error) {
      console.error("Error fetching chat getlist:", error);
    }
  };

  const handleImageError = (
    event: React.SyntheticEvent<HTMLImageElement, Event>
  ) => {
    event.currentTarget.src = "/img/admin_img/noimg.png"; // 대체 이미지 경로
  };

  // items가 2차원 배열의 첫 번째 요소로만 들어있기 때문에 이를 flatMap으로 펼치고,
  // reduce를 사용해 3개씩 나눠 새로운 2차원 배열을 생성

  useEffect(() => {
    chatlist();
    return () => {};
  }, []);
  return (
    <div className="container">
      <div className="OTO-con-1">
        <div className="OTO-con-2">
          <div className="OTO-con-3">
            <h3 style={{ fontWeight: "bold" }}>면접자 1:1 상담</h3>
          </div>
        </div>
      </div>

      <div className="OTO-main-con">
        {rows.map((row, rowIndex) => (
          <div className="OTO-main-con-row" key={rowIndex}>
            {row.map((item, colIndex) => (
              <div className="OTO-main-profileImgBox" key={colIndex}>
                <div>
                  <Link
                    to={{ pathname: `/consultant/OneToOne/detail` }}
                    state={{ chtno: item.chtno }}
                  >
                    <img
                      src={`http://localhost:80/mydream/${item.imgname}`}
                      alt="프로필"
                      className="OTO-main-profileImg"
                      onError={handleImageError}
                    />
                  </Link>
                </div>
                <div className="OTO-main-txtbox">
                  <div>
                    <h5 className="OTO-main-h5">{item.name}</h5>
                    <p>
                      <Link
                        to={{ pathname: `/consultant/OneToOne/detail` }}
                        state={{ chtno: item.chtno }}
                        className="OTO-main-msg"
                      >
                        <button className="feedback-nextbtn col-1">
                          상담하기
                        </button>
                      </Link>
                    </p>
                  </div>
                </div>
              </div>
            ))}
          </div>
        ))}
      </div>
    </div>
  );
};

export default OTO_main;
