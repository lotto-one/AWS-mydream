import React, { useState, useEffect } from "react";
import "./ConsultantAdminModal.css";

interface Career {
  cnsno: number;
  seqno: number;
  term: string;
  content: string;
  detail: string;
  careerdiv: "1" | "2"; // '1' for education, '2' for career
  credt: string;
  upddt: string;
}

interface CareerWithoutDates {
  cnsno: number;
  seqno: number;
  term: string;
  content: string;
  detail: string;
  careerdiv: "1" | "2";
}

interface Consult {
  cnsno: number;
  password: string;
  name: string;
  gendercd: string;
  categcd: string;
  phonenum: string;
  email: string;
  birthymd: string;
  cnscareer: string;
  cnsproject: string;
  cnscareer_vo: Career[];
  imgname: string;
  rolecd: string;
}

interface ConsultWithoutDates {
  cnsno: number;
  password: string;
  name: string;
  gendercd: string;
  categcd: string;
  phonenum: string;
  email: string;
  birthymd: string;
  cnscareer: string;
  cnsproject: string;
  cnscareer_vo: CareerWithoutDates[];
  imgname: string;
  rolecd: string;
}

interface ConsultantAdminModalProps {
  consultant: Consult;
  onSave: (consultant: ConsultWithoutDates) => void;
  onClose: () => void;
}

const ConsultantAdminModal: React.FC<ConsultantAdminModalProps> = ({
  consultant,
  onSave,
  onClose,
}) => {
  const [localConsultant, setLocalConsultant] = useState<Consult>(consultant);
  const [isCustomCareer, setIsCustomCareer] = useState<boolean>(false);
  const [isCustomProject, setIsCustomProject] = useState<boolean>(false);
  const [showPassword, setShowPassword] = useState<boolean>(false);

  const careerOptions = ["1년~3년", "3년~5년", "5년~10년", "10년 이상"];
  const projectOptions = ["10회 이상", "30회 이상", "50회 이상", "100회 이상"];

  useEffect(() => {
    setLocalConsultant(consultant);
    setIsCustomCareer(!careerOptions.includes(consultant.cnscareer));
    setIsCustomProject(!projectOptions.includes(consultant.cnsproject));
  }, [consultant]);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setLocalConsultant((prev) => ({ ...prev, [name]: value }));
  };

  const handleSelectChange = (
    e: React.ChangeEvent<HTMLSelectElement>,
    field: "cnscareer" | "cnsproject"
  ) => {
    const value = e.target.value;
    setLocalConsultant((prev) => ({
      ...prev,
      [field]: value === "직접 입력" ? "" : value,
    }));
    if (field === "cnscareer") {
      setIsCustomCareer(value === "직접 입력");
    } else if (field === "cnsproject") {
      setIsCustomProject(value === "직접 입력");
    }
  };

  const handleCareerChange = (
    e: React.ChangeEvent<HTMLInputElement>,
    index: number,
    careerType: "education" | "career"
  ) => {
    const { name, value } = e.target;
    setLocalConsultant((prev) => ({
      ...prev,
      cnscareer_vo: prev.cnscareer_vo.map((career) => {
        if (
          career.seqno === index &&
          career.careerdiv === (careerType === "education" ? "1" : "2")
        ) {
          return { ...career, [name]: value };
        }
        return career;
      }),
    }));
  };

  const handleAddCareerItem = (careerType: "education" | "career") => {
    const newCareer: Career = {
      cnsno: localConsultant.cnsno,
      seqno:
        Math.max(0, ...localConsultant.cnscareer_vo.map((c) => c.seqno)) + 1,
      term: "",
      content: "",
      detail: "",
      careerdiv: careerType === "education" ? "1" : "2",
      credt: new Date().toISOString(),
      upddt: new Date().toISOString(),
    };
    setLocalConsultant((prev) => ({
      ...prev,
      cnscareer_vo: [...prev.cnscareer_vo, newCareer],
    }));
  };

  const handleDeleteCareerItem = (
    seqno: number,
    careerType: "education" | "career"
  ) => {
    setLocalConsultant((prev) => ({
      ...prev,
      cnscareer_vo: prev.cnscareer_vo.filter(
        (career) =>
          !(
            career.seqno === seqno &&
            career.careerdiv === (careerType === "education" ? "1" : "2")
          )
      ),
    }));
  };

  const handleSave = () => {
    const updatedConsultant: ConsultWithoutDates = {
      ...localConsultant,
      cnscareer_vo: localConsultant.cnscareer_vo.map(
        ({ credt, upddt, ...rest }) => rest
      ),
    };
    onSave(updatedConsultant);
  };

  if (!localConsultant) {
    return null;
  }

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  return (
    <div className="consult-modal-overlay">
      <div className="consult-modal-content">
        <button className="consult-modal-close" onClick={onClose}>
          &times;
        </button>
        <h2>⚙️ Consultant 정보 입력</h2>
        <hr />
        <form className="consult-modal-form">
          <div className="consult-form-row">
            {/* <div className="consult-form-group">
              <label htmlFor="cnsno">컨설턴트 번호</label>
              <input type="number" id="cnsno" name="cnsno" value={localConsultant.cnsno || ''} onChange={handleChange} />
            </div> */}
            {/* <div className="form-group">
              <label htmlFor="password">비밀번호</label>
              <div className="password-input-container">
                <input
                  type={showPassword ? "text" : "password"}
                  id="password"
                  name="password"
                  value={localConsultant.password || ''}
                  onChange={handleChange}
                />
                <button type="button" onClick={togglePasswordVisibility} className="password-toggle">
                  {showPassword ? '숨기기' : '표시'}
                </button>
              </div>
            </div> */}
          </div>
          <div className="consult-form-row">
            <div className="consult-form-group">
              <label htmlFor="name">🍀 이름</label>
              <input
                type="text"
                id="name"
                name="name"
                value={localConsultant.name || ""}
                onChange={handleChange}
              />
            </div>
            <div className="consult-form-group">
              <label htmlFor="gendercd">🧑‍💼 성별</label>
              <select
                id="gendercd"
                name="gendercd"
                value={localConsultant.gendercd || ""}
                onChange={handleChange}
              >
                <option value="">선택</option>
                <option value="F">여성</option>
                <option value="M">남성</option>
              </select>
            </div>
          </div>
          <div className="consult-form-row">
            <div className="consult-form-group">
              <label htmlFor="categcd">👔 전문분야</label>
              <select
                id="categcd"
                name="categcd"
                value={localConsultant.categcd || ""}
                onChange={handleChange}
              >
                <option value="">선택</option>
                <option value="1">IT/개발</option>
                <option value="2">교육</option>
                <option value="3">영업/마케팅</option>
                <option value="4">기획/전략</option>
                <option value="5">경영</option>
              </select>
            </div>
            <div className="consult-form-group">
              <label htmlFor="phonenum">📞 연락처</label>
              <input
                type="text"
                id="phonenum"
                name="phonenum"
                value={localConsultant.phonenum || ""}
                onChange={handleChange}
              />
            </div>
          </div>
          <div className="consult-form-row">
            <div className="consult-form-group">
              <label htmlFor="email">✉️ 이메일</label>
              <input
                type="text"
                id="email"
                name="email"
                value={localConsultant.email || ""}
                onChange={handleChange}
              />
            </div>
            <div className="consult-form-group">
              <label htmlFor="birthymd">🎂 생년월일</label>
              <input
                type="text"
                id="birthymd"
                name="birthymd"
                value={localConsultant.birthymd || ""}
                onChange={handleChange}
              />
            </div>
          </div>
          <div className="consult-form-row">
            <div className="consult-form-group">
              <label htmlFor="cnscareer">💼 컨설팅경력</label>
              <select
                id="cnscareer"
                name="cnscareer"
                value={
                  isCustomCareer ? "직접 입력" : localConsultant.cnscareer || ""
                }
                onChange={(e) => handleSelectChange(e, "cnscareer")}
              >
                <option value="">선택</option>
                {careerOptions.map((option) => (
                  <option key={option} value={option}>
                    {option}
                  </option>
                ))}
                <option value="직접 입력">직접 입력</option>
              </select>
              {isCustomCareer && (
                <input
                  type="text"
                  name="cnscareer"
                  value={localConsultant.cnscareer || ""}
                  onChange={handleChange}
                />
              )}
            </div>
            <div className="consult-form-group">
              <label htmlFor="cnsproject">👥 컨설팅 프로젝트 수</label>
              <select
                id="cnsproject"
                name="cnsproject"
                value={
                  isCustomProject
                    ? "직접 입력"
                    : localConsultant.cnsproject || ""
                }
                onChange={(e) => handleSelectChange(e, "cnsproject")}
              >
                <option value="">선택</option>
                {projectOptions.map((option) => (
                  <option key={option} value={option}>
                    {option}
                  </option>
                ))}
                <option value="직접 입력">직접 입력</option>
              </select>
              {isCustomProject && (
                <input
                  type="text"
                  name="cnsproject"
                  value={localConsultant.cnsproject || ""}
                  onChange={handleChange}
                />
              )}
            </div>
          </div>
          <div className="consult-form-group">
            <hr />
            <label>경력 사항</label>
            {localConsultant.cnscareer_vo
              .filter((c) => c.careerdiv === "2")
              .map((career, index) => (
                <div key={index} className="consult-career-item">
                  <input
                    type="text"
                    name="term"
                    value={career.term}
                    onChange={(e) =>
                      handleCareerChange(e, career.seqno, "career")
                    }
                    placeholder="기간"
                  />
                  <input
                    type="text"
                    name="content"
                    value={career.content}
                    onChange={(e) =>
                      handleCareerChange(e, career.seqno, "career")
                    }
                    placeholder="내용"
                  />
                  <input
                    type="text"
                    name="detail"
                    value={career.detail}
                    onChange={(e) =>
                      handleCareerChange(e, career.seqno, "career")
                    }
                    placeholder="상세내용"
                  />
                  <button
                    type="button"
                    onClick={() =>
                      handleDeleteCareerItem(career.seqno, "career")
                    }
                  >
                    삭제
                  </button>
                </div>
              ))}
            <button type="button" onClick={() => handleAddCareerItem("career")}>
              ➕ 경력 추가
            </button>
          </div>
          <div className="consult-form-group">
            <hr />
            <label>학력 사항</label>
            {localConsultant.cnscareer_vo
              .filter((c) => c.careerdiv === "1")
              .map((career, index) => (
                <div key={index} className="consult-career-item">
                  <input
                    type="text"
                    name="term"
                    value={career.term}
                    onChange={(e) =>
                      handleCareerChange(e, career.seqno, "education")
                    }
                    placeholder="기간"
                  />
                  <input
                    type="text"
                    name="content"
                    value={career.content}
                    onChange={(e) =>
                      handleCareerChange(e, career.seqno, "education")
                    }
                    placeholder="내용"
                  />
                  <input
                    type="text"
                    name="detail"
                    value={career.detail}
                    onChange={(e) =>
                      handleCareerChange(e, career.seqno, "education")
                    }
                    placeholder="상세내용"
                  />
                  <button
                    type="button"
                    onClick={() =>
                      handleDeleteCareerItem(career.seqno, "education")
                    }
                  >
                    삭제
                  </button>
                </div>
              ))}
            <button
              type="button"
              onClick={() => handleAddCareerItem("education")}
            >
              ➕ 학력 추가
            </button>
          </div>
          <div className="consult-form-actions">
            <button type="button" onClick={handleSave}>
              저장
            </button>
            <button type="button" onClick={onClose}>
              취소
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ConsultantAdminModal;
