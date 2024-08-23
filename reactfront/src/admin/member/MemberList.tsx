import React, { useState, useEffect } from "react";
import MemberModal from "./MemberModal";
import "./Member.css"; // CSS 파일을 불러옴
import axios from "axios";

export interface MemberType {
  memno: number;
  id: number;
  email: string;
  name: string;
  nickname: string;
  gendercd: string;
  birthymd: string;
  consultant_name: string;
  status: string;
  credt: string;
  phonenum: string;
  categcd: string;
  loccd: string;
}
const categoryNameMapping: { [key: string]: string } = {
  "1": "IT/개발",
  "2": "교육",
  "3": "마케팅",
  "4": "기획",
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

const MemberList: React.FC = () => {
  const [loading, setLoading] = useState(false);
  const [members, setMembers] = useState<MemberType[]>([]);
  const [currentMember, setCurrentMember] = useState<MemberType | null>(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(10);
  const [totalItems, setTotalItems] = useState(0);
  const [popState, setPopState] = useState(false);

  useEffect(() => {
    const fetchMembers = async () => {
      setLoading(true);
      try {
        const response = await fetch(
          `${process.env.REACT_APP_BACK_END_URL}/admin/memadmin/with-consultants`
        );
        if (!response.ok) {
          throw new Error("네트워크 응답이 좋지 않습니다.");
        }

        let data: MemberType[] = await response.json();
        console.log(data);
        setTotalItems(data.length); // 총 아이템 수 설정
        data = data.map((member) => ({
          ...member,
          gendercd: member.gendercd === "M" ? "남" : "여",
          consultant_name: member.consultant_name
            ? member.consultant_name
            : "X",
          credt: formatDate(member.credt),
          categcd: categoryNameMapping[member.categcd] || "Unknown",
          loccd: loccdNameMapping[member.loccd] || "Unknown",
        }));
        setMembers(data);
      } catch (error) {
        console.error("멤버 리스트를 가져오는 도중 오류 발생:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchMembers();
  }, []);

  const handleDelete = async (memno: number) => {
    const confirmDelete = window.confirm("정말 삭제하시겠습니까?");
    if (confirmDelete) {
      try {
        console.log(memno);
        setLoading(true);
        const response = await axios.delete(
          `${process.env.REACT_APP_BACK_END_URL}/admin/memadmin/del/${memno}`
        );
        if (response.status === 204) {
          alert("멤버가 성공적으로 삭제되었습니다.");
          setMembers(members.filter((member) => member.memno !== memno));
        } else {
          alert("멤버 삭제에 실패했습니다.");
        }
      } catch (error) {
        console.error("멤버 삭제 중 오류가 발생했습니다:", error);
        alert("멤버 삭제 중 오류가 발생했습니다.");
      } finally {
        setLoading(false);
      }
    }
  };

  const formatDate = (dateString: string): string => {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    const hours = String(date.getHours()).padStart(2, "0");
    const minutes = String(date.getMinutes()).padStart(2, "0");

    return `${year}-${month}-${day} ${hours}:${minutes}`;
  };

  const showModal = (member: MemberType) => {
    setCurrentMember(member);
    setPopState(true);
  };

  const closeModal = () => {
    setPopState(false);
  };

  const handleSave = (updatedMember: MemberType) => {
    setMembers((prevMembers) =>
      prevMembers.map((member) =>
        member.id === updatedMember.id ? updatedMember : member
      )
    );
  };

  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = members.slice(indexOfFirstItem, indexOfLastItem);

  const handlePageChange = (pageNumber: number) => {
    setCurrentPage(pageNumber);
  };

  const totalPages = Math.ceil(totalItems / itemsPerPage);

  return (
    <div className="memberlist-sc1">
      <div className="member-container mt-5 membercon">
        <div className="member-header">
          <div className="member-header-text">회원 목록</div>
        </div>
        <div className="table-container">
          <table className="membertable">
            <thead>
              <tr className="table-header">
                <th className="table-email">이메일</th>
                <th>이름</th>
                <th>닉네임</th>
                <th className="table-gender">성별</th>
                <th>생년월일</th>
                <th>담당컨설턴트</th>
                <th>가입날짜</th>
                <th>회원관리</th>
              </tr>
            </thead>
            <tbody>
              {currentItems.map((member) => (
                <tr key={member.id} className="table-body">
                  <td className="table-email">{member.email}</td>
                  <td>{member.name}</td>
                  <td>{member.nickname}</td>
                  <td className="table-gender">{member.gendercd}</td>
                  <td>{member.birthymd}</td>
                  <td>{member.consultant_name}</td>
                  <td>{member.credt}</td>
                  <td>
                    <button
                      className="btn btn-primary btn-sm"
                      style={{ width: "80px", height: "40px", margin: "3px" }}
                      onClick={() => showModal(member)}
                    >
                      수정
                    </button>
                    <button
                      className="btn btn-danger btn-sm"
                      style={{ width: "80px", height: "40px", margin: "3px" }}
                      onClick={() => handleDelete(member.memno)}
                    >
                      삭제
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
            <tfoot className="table-foot">
              <div className="table-foot-div">
                <nav aria-label="Page navigation" className="pageblock">
                  <ul className="pagination">
                    <li
                      className={`page-item ${currentPage === 1 ? "disabled" : ""}`}
                    >
                      <a
                        className="page-link"
                        href="#"
                        onClick={() => handlePageChange(currentPage - 1)}
                      >
                        Previous
                      </a>
                    </li>
                    {Array.from({ length: totalPages }, (_, index) => (
                      <li
                        key={index + 1}
                        className={`page-item ${currentPage === index + 1 ? "active" : ""}`}
                      >
                        <a
                          className="page-link"
                          href="#"
                          onClick={() => handlePageChange(index + 1)}
                        >
                          {index + 1}
                        </a>
                      </li>
                    ))}
                    <li
                      className={`page-item ${currentPage === totalPages ? "disabled" : ""}`}
                    >
                      <a
                        className="page-link"
                        href="#"
                        onClick={() => handlePageChange(currentPage + 1)}
                      >
                        Next
                      </a>
                    </li>
                  </ul>
                </nav>
                <div className="table-foot-btn">
                  {/* <button
                    className="btn btn-primary mb-3"
                    onClick={() => window.location.reload()}
                  >
                    새로고침
                  </button> */}
                </div>
              </div>
            </tfoot>
          </table>
        </div>

        {popState && currentMember && (
          <MemberModal
            member={currentMember}
            onClose={closeModal}
            onSave={handleSave} // 추가된 부분
          />
        )}
      </div>
    </div>
  );
};

export default MemberList;
