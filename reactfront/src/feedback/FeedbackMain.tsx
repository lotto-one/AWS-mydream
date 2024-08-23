import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "./FeedbackMain.css";
import axios from "axios";

interface Item {
  name: string;
  imgname: string;
  memno: number;
}

const items: Item[] = [];

const FeedbackMain: React.FC = () => {
  const [items, setItems] = useState<Item[]>([]);
  const [cnsno, setCnsno] = useState<number | null>(
    localStorage.getItem("cnsno") !== null
      ? parseInt(localStorage.getItem("cnsno")!)
      : null
  );
  const handleImageError = (
    event: React.SyntheticEvent<HTMLImageElement, Event>
  ) => {
    event.currentTarget.src = "/img/admin_img/noimg.png"; // 대체 이미지 경로
  };

  useEffect(() => {
    const fetchData = async () => {
      const response = await axios.post(
        `${process.env.REACT_APP_BACK_END_URL}/consultant/feedback/main`,
        { cnsno: cnsno }
      );
      console.log(response.data);
      setItems(response.data);
    };
    fetchData();
  }, [cnsno]);

  const rows = items.reduce<Item[][]>((acc, item, index) => {
    if (index % 3 === 0) acc.push([]);
    acc[Math.floor(index / 3)].push(item);
    return acc;
  }, []);

  return (
    <div className="container">
      <div className="feedback-con-1">
        <div className="feedback-con-2">
          <div className="feedback-con-3">
            <h3 style={{ fontWeight: "bold" }}>담당 면접자 </h3>
          </div>
        </div>
      </div>
      <div className="feedback-main-con">
        {rows.map((row, rowIndex) => (
          <div className="feedback-main-con-row" key={rowIndex}>
            {row.map((item, colIndex) => (
              <div className="feedback-main-profileImgBox" key={colIndex}>
                <div className="feedback-imagebox">
                  <img
                    src={`http://localhost:80/mydream/${item.imgname}`}
                    onError={handleImageError}
                    style={{ width: "200px" }}
                    alt="프로필"
                    className="feedback-main-profileImg"
                  />
                </div>
                <div className="feedback-main-txtbox">
                  <div>
                    <h5 className="feedback-main-h5">{item.name}</h5>
                    <Link
                      to={`/consultant/feedback/resume?memno=${item.memno}`}
                    >
                      <button className="feedback-nextbtn col-1">이력서</button>
                    </Link>
                    <Link to={`/consultant/feedback/list?memno=${item.memno}`}>
                      <button className="feedback-nextbtn col-1">
                        면접 결과
                      </button>
                    </Link>
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

export default FeedbackMain;
