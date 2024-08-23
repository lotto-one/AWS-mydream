import React, { useState, useEffect } from "react";
import "./ConsultantAdmin.css";
import ConsultantAdminModal from "./ConsultantAdminModal";

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
  careerdiv: string;
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

const ConsultantAdmin: React.FC = () => {
  const [consultants, setConsultants] = useState<Consult[]>([]);
  const [showModal, setShowModal] = useState(false);
  const [currentConsultant, setCurrentConsultant] = useState<Consult | null>(
    null
  );
  const [isEditing, setIsEditing] = useState(false);

  useEffect(() => {
    fetchConsultants();
  }, []);

  const fetchConsultants = () => {
    fetch(`${process.env.REACT_APP_BACK_END_URL}/admin/adminConsult`, {
      method: "GET",
    })
      .then((response) => response.json())
      .then((data) => {
        setConsultants(data);
      })
      .catch((error) => {
        console.error("Error fetching data:", error);
      });
  };

  const handleAddClick = () => {
    setCurrentConsultant({
      cnsno: 0,
      password: "",
      name: "",
      gendercd: "",
      categcd: "",
      phonenum: "",
      email: "",
      birthymd: "",
      cnscareer: "",
      cnsproject: "",
      cnscareer_vo: [],
      imgname: "noimage.png",
      rolecd: "C",
    });
    setIsEditing(false);
    setShowModal(true);
  };

  const handleEditClick = (consultant: Consult) => {
    setCurrentConsultant(consultant);
    setIsEditing(true);
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
  };

  const handleSaveConsultant = (consultant: ConsultWithoutDates) => {
    const url = isEditing
      ? `${process.env.REACT_APP_BACK_END_URL}/admin/adminConsult/${consultant.cnsno}/updateConsultant`
      : `${process.env.REACT_APP_BACK_END_URL}/admin/membership/register2`;

    fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(consultant),
    })
      .then((response) => {
        console.log("Response status:", response.status);
        console.log("Response headers:", response.headers);

        // 응답 상태가 200 OK일 때 처리
        if (response.ok) {
          // 응답 본문이 JSON 형식인지 확인 후 파싱
          return response.json().then((data) => {
            console.log("Response body:", data);
            // 서버에서 성공적인 응답 본문을 받으면
            fetchConsultants();
            setShowModal(false);
            alert("컨설턴트 정보가 성공적으로 저장되었습니다.");
          });
        } else {
          // 응답 상태가 200 OK가 아닐 때
          return response.text().then((text) => {
            throw new Error(text || "Network response was not ok");
          });
        }
      })
      .catch((error) => {
        console.error("Error saving consultant:", error);
        alert(`컨설턴트 저장 중 오류가 발생했습니다: ${error.message}`);
      });
  };

  const handleDeleteConsultant = (cnsno: number) => {
    if (
      window.confirm(
        "정말로 이 컨설턴트를 삭제하시겠습니까? 모든 관련 정보(경력, 학력 포함)가 삭제됩니다."
      )
    ) {
      fetch(
        `${process.env.REACT_APP_BACK_END_URL}/admin/adminConsult/${cnsno}/delConsultant`,
        {
          method: "DELETE",
        }
      )
        .then((response) => {
          if (!response.ok) {
            throw new Error("Failed to delete consultant");
          }
          return response.text(); // 응답이 비어있을 수 있으므로 text()로 처리
        })
        .then((data) => {
          fetchConsultants();
          alert("컨설턴트와 관련 정보가 성공적으로 삭제되었습니다.");
        })
        .catch((error) => {
          console.error("Error deleting consultant:", error);
          alert(
            "컨설턴트 삭제 중 오류가 발생했습니다. 모든 관련 정보가 삭제되지 않았을 수 있습니다."
          );
        });
    }
  };

  const getGenderText = (gendercd: string) => {
    switch (gendercd) {
      case "F":
        return "여성";
      case "M":
        return "남성";
      default:
        return "알 수 없음";
    }
  };

  const getCategoryText = (categcd: string) => {
    switch (categcd) {
      case "1":
        return "IT/개발";
      case "2":
        return "교육";
      case "3":
        return "영업/마케팅";
      case "4":
        return "기획/전략";
      case "5":
        return "경영";
      default:
        return "알 수 없음";
    }
  };

  return (
    <div className="table-container">
      <div className="table-title">🪪 Consultant List</div>
      <button className="btn-add" onClick={handleAddClick}>
        ➕ Consultant 추가
      </button>
      <table className="user-table">
        <thead>
          <tr>
            <th>번호</th>
            <th>컨설턴트번호</th>
            <th>이름</th>
            <th>성별</th>
            <th>전문분야</th>
            <th>연락처</th>
            <th>이메일</th>
            <th>생년월일</th>
            <th>컨설팅경력</th>
            <th>프로젝트수행</th>
            <th>수정/삭제</th>
          </tr>
        </thead>
        <tbody>
          {consultants.map((consultant, index) => {
            const educationItems = consultant.cnscareer_vo.filter(
              (item) => item.careerdiv === "1"
            );
            const careerItems = consultant.cnscareer_vo.filter(
              (item) => item.careerdiv === "2"
            );

            return (
              <React.Fragment key={consultant.cnsno}>
                <tr>
                  <td rowSpan={educationItems.length + careerItems.length + 3}>
                    {index + 1}
                  </td>
                  <td>{consultant.cnsno}</td>
                  <td>{consultant.name}</td>
                  <td>{getGenderText(consultant.gendercd)}</td>
                  <td>{getCategoryText(consultant.categcd)}</td>
                  <td>{consultant.phonenum}</td>
                  <td>{consultant.email}</td>
                  <td>{consultant.birthymd}</td>
                  <td>{consultant.cnscareer}</td>
                  <td>{consultant.cnsproject}</td>
                  <td rowSpan={educationItems.length + careerItems.length + 3}>
                    <button
                      className="btn-up"
                      onClick={() => handleEditClick(consultant)}
                    >
                      수정
                    </button>
                    <button
                      className="btn-del"
                      onClick={() => handleDeleteConsultant(consultant.cnsno)}
                    >
                      삭제
                    </button>
                  </td>
                </tr>

                {educationItems.length > 0 && (
                  <tr className="education">
                    <td colSpan={9}>
                      <strong>학력사항</strong>
                    </td>
                  </tr>
                )}
                {educationItems.map((item) => (
                  <tr key={`edu-${consultant.cnsno}-${item.seqno}`}>
                    <td colSpan={3}>{item.term}</td>
                    <td colSpan={2}>{item.content}</td>
                    <td colSpan={4}>{item.detail}</td>
                  </tr>
                ))}

                {careerItems.length > 0 && (
                  <tr className="career">
                    <td colSpan={9}>
                      <strong>경력사항</strong>
                    </td>
                  </tr>
                )}
                {careerItems.map((item) => (
                  <tr key={`car-${consultant.cnsno}-${item.seqno}`}>
                    <td colSpan={3}>{item.term}</td>
                    <td colSpan={2}>{item.content}</td>
                    <td colSpan={4}>{item.detail}</td>
                  </tr>
                ))}
              </React.Fragment>
            );
          })}
        </tbody>
      </table>

      {showModal && currentConsultant && (
        <ConsultantAdminModal
          consultant={currentConsultant}
          onSave={handleSaveConsultant}
          onClose={handleCloseModal}
        />
      )}
    </div>
  );
};

export default ConsultantAdmin;
