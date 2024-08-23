import React, { useState, useEffect } from "react";
import { ProfileData } from "./types";

import {
  Container,
  Header,
  ProfileImage,
  HeaderInfo,
  Name,
  Gender,
  Title,
  TabContainer,
  Tab,
  Section,
  SectionTitle,
  InfoGrid,
  InfoItem,
  InfoIcon,
  InfoText,
  EducationItem,
  Year,
  Details,
  School,
  Degree,
  Introduce,
  EditButton,
  Modal,
  ModalContent,
  ModalHeader,
  ModalBody,
  ModalFooter,
  Form,
  FormGroup,
  Label,
  Input,
  Button,
  CancelButton,
  Textarea,
  FileInput,
  UploadImage,
} from "./ConsultantProfile.styles";

const ConsultantProfile: React.FC = () => {
  const [activeTab, setActiveTab] = useState("info");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isUploadModalOpen, setIsUploadModalOpen] = useState(false);
  const [profileData, setProfileData] = useState<ProfileData | null>(null);
  const [editedProfile, setEditedProfile] = useState<ProfileData | null>(null);
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);

  useEffect(() => {
    fetchProfileData();
  }, []);

  const cnsno = localStorage.getItem("cnsno");

  const fetchProfileData = async () => {
    try {
      const response = await fetch(
        `${process.env.REACT_APP_BACK_END_URL}/consultant/consultProfiles/${cnsno}`
      );
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      const data: ProfileData = await response.json();
      setProfileData(data);
      setEditedProfile(data);
    } catch (error) {
      console.error("Error fetching profile data:", error);
    }
  };

  // 성별과 카테고리 텍스트 변환 함수
  const getGenderText = (gendercd: string) => {
    switch (gendercd) {
      case "F":
        return "여성";
      case "M":
        return "남성";
      default:
        return "기타";
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
      default:
        return "경영";
    }
  };

  const handleEditClick = () => setIsModalOpen(true);

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setPassword("");
    setConfirmPassword("");
    setShowConfirmPassword(false);
  };

  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setEditedProfile((prev) => (prev ? { ...prev, [name]: value } : null));
  };

  const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    if (name === "password") {
      setPassword(value);
      setShowConfirmPassword(value.length > 0);
    } else if (name === "confirmPassword") {
      setConfirmPassword(value);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (editedProfile && password === confirmPassword) {
      try {
        const response = await fetch(
          `${process.env.REACT_APP_BACK_END_URL}/consultant/consultProfiles/${cnsno}`,
          {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ ...editedProfile, password }),
          }
        );
        if (!response.ok) {
          if (response.status === 404) {
            throw new Error("프로필을 찾을 수 없습니다.");
          }
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        const updatedProfile = await response.json();
        setProfileData(updatedProfile);
        setIsModalOpen(false);
      } catch (error) {
        console.error("Error updating profile:", error);
      }
    } else if (password !== confirmPassword) {
      alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
    }
  };

  const handleUploadClick = () => setIsUploadModalOpen(true);

  const handleCloseUploadModal = () => {
    setIsUploadModalOpen(false);
    setSelectedFile(null);
  };

  const handleImageError = (
    event: React.SyntheticEvent<HTMLImageElement, Event>
  ) => {
    event.currentTarget.src = "/img/admin_img/noimg.png"; // 대체 이미지 경로
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      setSelectedFile(e.target.files[0]);
    }
  };
  {
    /* 🐛🐛🐛  */
  }
  const handleFileUpload = async () => {
    if (selectedFile) {
      const formData = new FormData();
      formData.append("imgfile", selectedFile);

      try {
        const uploadResponse = await fetch(
          `${process.env.REACT_APP_BACK_END_URL}/consultant/consultProfiles/uploadProfileImage`,
          {
            method: "POST",
            body: formData,
          }
        );

        if (!uploadResponse.ok) {
          throw new Error(`HTTP error! Status: ${uploadResponse.status}`);
        }

        const uploadResult = await uploadResponse.json();
        const filename = uploadResult.filename;

        // 프로필 이미지 업데이트 요청
        const updateResponse = await fetch(
          `${process.env.REACT_APP_BACK_END_URL}/consultant/consultProfiles/${cnsno}/updateProfileImage`,
          {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ imgname: filename }),
          }
        );

        if (!updateResponse.ok) {
          throw new Error(`HTTP error! Status: ${updateResponse.status}`);
        }

        // 프로필 데이터 다시 불러오기
        await fetchProfileData();
        handleCloseUploadModal();

        // 프로필 이미지 즉시 업데이트
        setProfileData((prevData) =>
          prevData ? { ...prevData, imgname: filename } : null
        );
      } catch (error) {
        console.error("Error uploading file:", error);
        alert("파일 업로드 중 오류가 발생했습니다.");
      }
    }
  };
  {
    /* 🐛🐛🐛  */
  }

  if (!profileData) {
    return <div>Loading...</div>;
  }

  return (
    <Container>
      <Header>
        {/* 🐛🐛🐛  */}
        <ProfileImage
          src={
            profileData.imgname
              ? `${process.env.REACT_APP_BACK_END_URL}/uploads/${profileData.imgname}`
              : `${process.env.REACT_APP_BACK_END_URL}/img/noimage.png`
          }
          alt={profileData.name}
          onError={handleImageError}
        />
        <UploadImage
          src="/img/camera.png"
          alt="프로필 사진 수정"
          onClick={handleUploadClick}
        />
        {/* 🐛🐛🐛  */}
        <HeaderInfo>
          <div>
            <Name as="h1">
              {profileData.name}{" "}
              <Gender>({getGenderText(profileData.gendercd)})</Gender>
            </Name>
            <Title as="h2">
              {getCategoryText(profileData.categcd)} 전문 컨설턴트
            </Title>
          </div>
          <EditButton onClick={handleEditClick}>프로필 수정</EditButton>
        </HeaderInfo>
      </Header>

      <TabContainer>
        <Tab active={activeTab === "info"} onClick={() => setActiveTab("info")}>
          기본 정보
        </Tab>
        <Tab
          active={activeTab === "education"}
          onClick={() => setActiveTab("education")}
        >
          학력 및 경력
        </Tab>
      </TabContainer>

      {activeTab === "info" && (
        <>
          <Section>
            <SectionTitle as="h3">기본 정보</SectionTitle>
            <InfoGrid>
              <InfoItem>
                <InfoIcon>📞</InfoIcon>
                <InfoText>{profileData.phonenum}</InfoText>
              </InfoItem>
              <InfoItem>
                <InfoIcon>✉️</InfoIcon>
                <InfoText>{profileData.email}</InfoText>
              </InfoItem>
              <InfoItem>
                <InfoIcon>🎂</InfoIcon>
                <InfoText>{profileData.birthymd}</InfoText>
              </InfoItem>
            </InfoGrid>
          </Section>
          <Section>
            <SectionTitle as="h3">주요 경력</SectionTitle>
            <InfoGrid>
              <InfoItem>
                <InfoIcon>💼</InfoIcon>
                <InfoText>컨설팅 경력: {profileData.cnscareer}</InfoText>
              </InfoItem>
              <InfoItem>
                <InfoIcon>👥</InfoIcon>
                <InfoText>프로젝트 수행: {profileData.cnsproject}</InfoText>
              </InfoItem>
            </InfoGrid>
          </Section>
          <Section>
            <SectionTitle as="h3">자기 소개</SectionTitle>
            <Introduce>{profileData.introduce}</Introduce>
          </Section>
        </>
      )}

      {activeTab === "education" && (
        <>
          <Section>
            <SectionTitle as="h3">학력</SectionTitle>
            {profileData.cnscareer_vo
              .filter((career) => career.careerdiv === "1")
              .map((edu, index) => (
                <EducationItem key={index}>
                  <Year>{edu.term}</Year>
                  <Details>
                    <School>{edu.content}</School>
                    <Degree>{edu.detail}</Degree>
                  </Details>
                </EducationItem>
              ))}
          </Section>
          <Section>
            <SectionTitle as="h3">상세 경력</SectionTitle>
            {profileData.cnscareer_vo
              .filter((career) => career.careerdiv === "2")
              .map((career, index) => (
                <EducationItem key={index}>
                  <Year>{career.term}</Year>
                  <Details>
                    <School>{career.content}</School>
                    <Degree>{career.detail}</Degree>
                  </Details>
                </EducationItem>
              ))}
          </Section>
        </>
      )}

      <Modal isOpen={isModalOpen}>
        <ModalContent>
          <ModalHeader>
            <h2>⚙️ 프로필 수정</h2>
            <button onClick={handleCloseModal}>&times;</button>
          </ModalHeader>
          <ModalBody>
            <Form onSubmit={handleSubmit}>
              <FormGroup>
                <Label htmlFor="name">🍀 이름</Label>
                <Input
                  type="text"
                  id="name"
                  name="name"
                  value={editedProfile?.name || ""}
                  onChange={handleInputChange}
                  disabled
                />
              </FormGroup>
              <FormGroup>
                <Label htmlFor="categcd">👔 전문 분야</Label>
                <Input
                  type="text"
                  id="categcd"
                  name="categcd"
                  value={getCategoryText(editedProfile?.categcd || "")}
                  onChange={handleInputChange}
                  disabled
                />
              </FormGroup>
              <FormGroup>
                <Label htmlFor="password">🔒 새 비밀번호</Label>
                <Input
                  type="password"
                  id="password"
                  name="password"
                  value={password}
                  onChange={handlePasswordChange}
                />
              </FormGroup>
              {showConfirmPassword && (
                <FormGroup>
                  <Label htmlFor="confirmPassword">🔒 비밀번호 확인</Label>
                  <Input
                    type="password"
                    id="confirmPassword"
                    name="confirmPassword"
                    value={confirmPassword}
                    onChange={handlePasswordChange}
                  />
                </FormGroup>
              )}
              <FormGroup>
                <Label htmlFor="phonenum">📞 연락처</Label>
                <Input
                  type="tel"
                  id="phonenum"
                  name="phonenum"
                  value={editedProfile?.phonenum || ""}
                  onChange={handleInputChange}
                />
              </FormGroup>
              <FormGroup>
                <Label htmlFor="email">✉️ 이메일</Label>
                <Input
                  type="email"
                  id="email"
                  name="email"
                  value={editedProfile?.email || ""}
                  onChange={handleInputChange}
                />
              </FormGroup>
              <FormGroup>
                <Label htmlFor="birthymd">🎂 생년월일</Label>
                <Input
                  type="text"
                  id="birthymd"
                  name="birthymd"
                  value={editedProfile?.birthymd || ""}
                  onChange={handleInputChange}
                />
              </FormGroup>
              <FormGroup>
                <Label htmlFor="cnscareer">💼 컨설팅 경력</Label>
                <Input
                  type="text"
                  id="cnscareer"
                  name="cnscareer"
                  value={editedProfile?.cnscareer || ""}
                  onChange={handleInputChange}
                  disabled
                />
              </FormGroup>
              <FormGroup>
                <Label htmlFor="cnsproject">👥 프로젝트 수행</Label>
                <Input
                  type="text"
                  id="cnsproject"
                  name="cnsproject"
                  value={editedProfile?.cnsproject || ""}
                  onChange={handleInputChange}
                  disabled
                />
              </FormGroup>
              <FormGroup>
                <Label htmlFor="introduce">📖 자기소개</Label>
                <Textarea
                  id="introduce"
                  name="introduce"
                  value={editedProfile?.introduce || ""}
                  onChange={handleInputChange}
                />
              </FormGroup>
              <ModalFooter>
                <Button type="submit">저장</Button>
                <CancelButton type="button" onClick={handleCloseModal}>
                  취소
                </CancelButton>
              </ModalFooter>
            </Form>
          </ModalBody>
        </ModalContent>
      </Modal>

      <Modal isOpen={isUploadModalOpen}>
        <ModalContent>
          <ModalHeader>
            <h2>프로필 사진 수정</h2>
            <button onClick={handleCloseUploadModal}>&times;</button>
          </ModalHeader>
          <ModalBody>
            <Form>
              <FormGroup>
                <Label htmlFor="file">📷 프로필 사진 선택</Label>
                <FileInput
                  type="file"
                  id="file"
                  name="file"
                  accept="image/*"
                  onChange={handleFileChange}
                />
              </FormGroup>
              <ModalFooter>
                <Button type="button" onClick={handleFileUpload}>
                  업로드
                </Button>
                <CancelButton type="button" onClick={handleCloseUploadModal}>
                  취소
                </CancelButton>
              </ModalFooter>
            </Form>
          </ModalBody>
        </ModalContent>
      </Modal>
    </Container>
  );
};

export default ConsultantProfile;
