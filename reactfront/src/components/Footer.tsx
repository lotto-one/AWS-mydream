import React, { useEffect, useState } from 'react';
import './Footer.css';

const Footer: React.FC = () => {
  const [isHidden, setIsHidden] = useState(true);

  const handleScroll = () => {
    const scrollTop = window.scrollY;
    const windowHeight = window.innerHeight;
    const documentHeight = document.documentElement.scrollHeight;
    setIsHidden(!(scrollTop + windowHeight >= documentHeight));
  };

  useEffect(() => {
    window.addEventListener('scroll', handleScroll);
    handleScroll(); // 초기 로드 시 스크롤 상태 확인
    return () => {
      window.removeEventListener('scroll', handleScroll);
    };
  }, []);

  return (
    <div className={`footer ${isHidden ? 'hidden' : ''}`}>
      <p style={{ fontSize: '15px', color: 'white', marginBottom: '0px' }}>
        &copy; 치타 : 배 회장, 서 이사, 안 사장, 복 팀장, 최 부장, 추 차장, 방 과장 / 2024.07.25
      </p>
    </div>
  );
};

export default Footer;
