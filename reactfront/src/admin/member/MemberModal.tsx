import React, { useState, useEffect } from "react";
import axios from "axios";
import "./MemberModal.css";
import { MemberType } from "./MemberList";

const categoryNameMapping: { [key: string]: string } = {
  "1": "IT/개발",
  "2": "교육",
  "3": "영업/마케팅",
  "4": "기획/전략",
  "5": "경영",
};

const loccdNameMapping: { [key: string]: string } = {
  "1": "서울",
  "2": "경기도",
  "3": "충청도",
  "4": "전라도",
  "5": "경상도",
  "6": "강원도",
  "7": "제주도",
};

const genderMapping: { [key: string]: string } = {
  M: "남",
  F: "여",
};

interface MemberModalProps {
  member: MemberType;
  onClose: () => void;
  onSave: (updatedMember: MemberType) => void;
}

const MemberModal: React.FC<MemberModalProps> = ({
  member,
  onClose,
  onSave,
}) => {
  const [email, setEmail] = useState(member.email);
  const [name, setName] = useState(member.name);
  const [nickname, setNickname] = useState(member.nickname);
  const [gendercd, setGendercd] = useState(member.gendercd);
  const [birthymd, setBirthymd] = useState(member.birthymd);
  const [categcd, setCategcd] = useState(member.categcd);
  const [loccd, setLoccd] = useState(member.loccd);
  const [credt, setCredt] = useState(member.credt);
  const [phonenum, setPhonenum] = useState(member.phonenum);
  const [memno, setMemno] = useState(member.memno);
  const categKey = Object.keys(categoryNameMapping).find(
    (key) => categoryNameMapping[key] === member.categcd
  );
  const locKey = Object.keys(loccdNameMapping).find(
    (key) => loccdNameMapping[key] === member.loccd
  );
  const genderKey = Object.keys(genderMapping).find(
    (key) => genderMapping[key] === member.gendercd
  );

  useEffect(() => {
    console.log("Received member:", member); // 데이터 로드 확인
    setEmail(member.email);
    setName(member.name);
    setNickname(member.nickname);
    setGendercd(member.gendercd);
    setBirthymd(member.birthymd);
    setCategcd(categKey ? categKey : "");
    setLoccd(locKey ? locKey : ""); // 매핑된 키가 없을 경우 빈 문자열로 설정
    setCredt(member.credt);
    setPhonenum(member.phonenum);
    setMemno(member.memno);
    setGendercd(genderKey ? genderKey : "");
  }, [member]);
  // 상태 확인
  console.log("categcd:", categcd);
  console.log("loccd:", loccd);
  console.log("gendercd:", gendercd);
  console.log(typeof categcd);

  const formatDate = (dateString: string): string => {
    if (dateString.length === 8) {
      const year = dateString.substring(0, 4);
      const month = parseInt(dateString.substring(4, 6), 10);
      const day = parseInt(dateString.substring(6, 8), 10);
      return `${year}년 ${month}월 ${day}일`;
    }
    return dateString;
  };

  const handleCategcdChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setCategcd(e.target.value);
  };

  const handleLoccdChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setLoccd(e.target.value);
  };

  const handleGenderChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setGendercd(e.target.value);
  };

  const handleSave = async () => {
    const updatedMember: MemberType = {
      ...member,
      email,
      name,
      nickname,
      gendercd,
      categcd,
      loccd,
      phonenum,
      memno,
    };
    console.log(updatedMember);
    try {
      const response = await axios.put(
        `${process.env.REACT_APP_BACK_END_URL}/admin/memadmin/user-update/${member.memno}`,
        updatedMember,
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      alert("회원 정보가 성공적으로 업데이트되었습니다.");
      onSave(updatedMember);
      window.location.reload();
    } catch (error) {
      console.error("Error updating member:", error);
      alert("회원 정보 업데이트 중 오류가 발생했습니다.");
    }
  };

  return (
    <div className="modal-overlay99">
      <div className="modal-content99">
        <button className="close-button99" onClick={onClose}>
          X
        </button>
        <h2>회원 정보 수정</h2>
        <form>
          <div className="form-group99">
            <label>이메일</label>
            <input
              type="text"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>
          <div className="form-group99">
            <label>이름</label>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
            />
          </div>
          <div className="form-group99">
            <label>닉네임</label>
            <input
              type="text"
              value={nickname}
              onChange={(e) => setNickname(e.target.value)}
            />
          </div>
          <div className="form-group99">
            <label>성별</label>
            <select value={gendercd} onChange={handleGenderChange}>
              {Object.entries(genderMapping).map(([key, value]) => (
                <option key={key} value={key}>
                  {value}
                </option>
              ))}
            </select>
          </div>
          <div className="form-group99">
            <label>전화번호</label>
            <input
              type="text"
              value={phonenum}
              onChange={(e) => setPhonenum(e.target.value)}
            />
          </div>
          <div className="form-group99">
            <label>카테고리</label>
            <select value={categcd} onChange={handleCategcdChange}>
              {Object.entries(categoryNameMapping).map(([key, value]) => (
                <option key={key} value={key}>
                  {value}
                </option>
              ))}
            </select>
          </div>
          <div className="form-group99">
            <label>지역</label>
            <select value={loccd} onChange={handleLoccdChange}>
              {Object.entries(loccdNameMapping).map(([key, value]) => (
                <option key={key} value={key}>
                  {value}
                </option>
              ))}
            </select>
          </div>
          <div className="form-group99">
            <label>생년월일</label>
            <input type="text" value={formatDate(birthymd)} disabled />
          </div>
          <div className="form-group99">
            <label>가입날짜</label>
            <input type="text" value={credt} readOnly disabled />
          </div>
          <div className="form-group99">
            <button type="button" className="form-btn99" onClick={handleSave}>
              저장
            </button>
            <button type="button" className="form-btn98" onClick={onClose}>
              닫기
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default MemberModal;
