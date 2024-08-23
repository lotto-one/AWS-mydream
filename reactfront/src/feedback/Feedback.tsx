import React, { useEffect, useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import './Feedback.css';
import axios from 'axios';

interface Item {
  name: string;
  upddt: string;
  intno: number;
}
const Feedback = () => {
  const location = useLocation();
  const [items, setItems] = useState<Item[]>([]);
  const [memno, setMemno] = useState<number | null>();
  
  useEffect(() => {
    const searchParams = new URLSearchParams(location.search);
    const memno = searchParams.get('memno');
      const fetchData = async() => {
        console.log("memno:", memno);
        const response = await axios.post(`${process.env.REACT_APP_BACK_END_URL}/consultant/feedback/feedbkList`, {"memno": memno});
        console.log(response.data);
        setItems(response.data);
        }
      fetchData();
  },[memno]
)

  return (
    <div className="container">
      <div className="feed-con-1">
        <div className="feed-con-2">
          <div className="feed-con-3">
            <h3 style={{ fontWeight: 'bold' }}>피드백 관리</h3>
          </div>
        </div>
      </div>
      <div className="feed-con">
        {items.map((item, index) => (
          <div className="feed-item" key={index}>
            <table className='feed-table'>
                <thead>
                <tr className='feed-tr'>
                    <td className='feed-td'>
                        <Link to={`/consultant/feedback/detail?intno=${item.intno}`} className='feed-link'>
                            <p className='feed-p'>{item.name} 님 직무면접결과 &nbsp; {item.upddt.split('T')[0]}</p>
                        </Link>
                    </td>
                </tr>
                </thead>
            </table>
            </div>
        ))}
      </div>
    </div>
  );
}

export default Feedback;
