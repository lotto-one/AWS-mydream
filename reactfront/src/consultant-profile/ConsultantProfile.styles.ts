import styled from 'styled-components';
import { TabProps } from './types';

export const Container = styled.div`
  && {
    max-width: 1280px;
    margin: 0 auto;
    padding: 40px 20px;
    font-family: 'NanumSquare', sans-serif;
    background-color: #f8f9fa;
    border-radius: 12px;
    box-shadow: 0 5px 15px rgba(0,0,0,0.1);
  }
`;

export const Header = styled.header`
  && {
    display: flex;
    align-items: center;
    margin-bottom: 30px;
  }
`;

export const ProfileImage = styled.img`
  && {
    width: 250px;
    height: 200px;
    border-radius: 50%;
    object-fit: cover;
    margin-left: 40px;
    margin-right: 30px;
    box-shadow: 0 4px 10px rgba(0,0,0,0.1);
  }
`;

export const UploadImage = styled.img`
  && {
    margin-left: -70px;
    margin-top: 100px;
    cursor: pointer;
  }
`;

export const HeaderInfo = styled.div`
  && {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
  }
`;

export const Name = styled.div`
  && {
    font-size: 1.5rem;
    font-weight: bold;
    color: #2c3e50;
    margin: 0 0 15px 0;
  }
`;

export const Gender = styled.span`
  && {
    font-size: 1.2rem;
    color: #7f8c8d;
  }
`;

export const Title = styled.div`
  && {
    font-size: 1.2rem;
    font-weight: bold;
    color: #34495e;
    margin-left: 20px;
  }
`;

export const TabContainer = styled.div`
  && {
    display: flex;
    margin-bottom: 30px;
    border-bottom: 2px solid #e0e0e0;
  }
`;

export const Tab = styled.button<TabProps>`
  && {
    padding: 10px 20px;
    font-size: 1.1rem;
    font-weight: 600;
    background: none;
    border: none;
    color: ${props => props.active ? '#1659de' : '#7f8c8d'};
    font-weight: ${props => props.active ? '900' : '600'};
    border-bottom: ${props => props.active ? '5px solid #003d8c' : 'none'};
    cursor: pointer;
    transition: all 0.3s ease;

    &:hover {
      color: #3498db;
    }
  }
`;

export const Section = styled.section`
  && {
    background-color: white;
    padding: 25px;
    margin-bottom: 25px;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0,0,0,0.05);
  }
`;

export const SectionTitle = styled.div`
  && {
    font-size: 1.1rem;
    font-weight: bold;
    color: #2c3e50;
    margin-bottom: 20px;
    padding-bottom: 10px;
    border-bottom: 2px solid #ecf0f1;
  }
`;

export const InfoGrid = styled.div`
  && {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 20px;
  }
`;

export const InfoItem = styled.div`
  && {
    display: flex;
    align-items: center;
  }
`;

export const InfoIcon = styled.span`
  && {
    font-size: 1.1rem;
    margin-right: 10px;
  }
`;

export const InfoText = styled.span`
  && {
    font-size: 1.1rem;
    color: #34495e;
  }
`;

export const TagContainer = styled.div`
  && {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    margin-bottom: 20px;
  }
`;

export const Tag = styled.span`
  && {
    background-color: #1659de;
    color: #ffffff;
    padding: 5px 10px;
    border-radius: 4px;
  }
`;

export const EducationItem = styled.div`
  && {
    display: flex;
    margin-bottom: 20px;
  }
`;

export const Year = styled.span`
  && {
    font-weight: bold;
    margin-right: 20px;
    min-width: 60px;
    font-size: 1.1rem;
  }
`;

export const Details = styled.div`
  && {
    flex: 1;
    text-align: left;
  }
`;

export const School = styled.div`
  && {
    font-weight: bold;
    margin-bottom: 5px;
    font-size: 1.1rem;
    text-align: left;
  }
`;

export const Degree = styled.div`
  && {
    color: #7f8c8d;
    text-align: left;
  }
`;

export const Introduce = styled.p`
  && {
    font-size: 1.1rem;
    line-height: 1.6;
    color: #34495e;
    text-align: left;
  }
`;

export const EditButton = styled.button`
  && {
    background-color: #1659de;
    color: white;
    border: none;
    padding: 12px 25px;
    border-radius: 4px;
    font-size: 1.1rem;
    font-weight: 600;
    width: 150px;
    cursor: pointer;
    transition: background-color 0.3s ease;
    margin-right: 40px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);

    &:hover {
      background-color: #003d8c;
    }
  }
`;

// 프로필 수정 모달폼 관련 스타일
export const Modal = styled.div<{ isOpen: boolean }>`
  && {
    display: ${props => props.isOpen ? 'flex' : 'none'};
    position: fixed;
    z-index: 1000;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    overflow: auto;
    background-color: rgba(0,0,0,0.4);
    justify-content: center;
    align-items: center;
  }
`;

export const ModalContent = styled.div`
  && {
    background-color: #fefefe;
    padding: 20px;
    border-radius: 12px;
    width: 80%;
    max-width: 700px;
    box-shadow: 0 5px 15px rgba(0,0,0,0.3);
  }
`;

export const ModalHeader = styled.div`
  && {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    h2 {
      color: #2c3e50;
      text-align: center;
      font-weight: 600
    }

    button {
      background: none;
      border: none;
      font-size: 1.5rem;
      cursor: pointer;
      color: #7f8c8d;
    }
  }
`;

export const ModalBody = styled.div`
  && {
    margin-bottom: 20px;
  }
`;

export const ModalFooter = styled.div`
  && {
    display: flex;
    justify-content: flex-end;
  }
`;

export const Form = styled.form`
  && {
    display: flex;
    flex-direction: column;
  }
`;

export const FormGroup = styled.div`
  && {
    margin-bottom: 10px;
    display: flex;
  }
`;

export const Label = styled.label`
  && {
    display: flex;
    margin-bottom: 5px;
    font-weight: bold;
    color: #2c3e50;
    flex: 5 0 30%;
    justify-content: flex-start;
    align-items: center;
    font-size: 1.1rem;
  }
`;

export const Input = styled.input`
  && {
    width: 100%;
    padding: 0.5em;
    border: 1px solid #ccc;
    border-radius: 4px;
    box-sizing: border-box;
    font-size: 1.1rem;
    font-family: sans-serif;
  }
`;

export const FileInput = styled.input`
  && {
    width: 100%;
    padding: 0.5em;
    border: 1px solid #ccc;
    border-radius: 4px;
    box-sizing: border-box;
    font-size: 1em;
    font-family: sans-serif;
    background-color: #ffffff;
    cursor: pointer;
  }
`;

export const UploadButton = styled.button`
  && {
    background-color: #1659de;
    color: white;
    border: none;
    padding: 10px 20px;
    border-radius: 4px;
    font-size: 1rem;
    font-weight: 600;
    cursor: pointer;
    transition: background-color 0.3s ease;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);

    &:hover {
      background-color: #003d8c;
    }
  }
`;

export const Button = styled.button`
  && {
    background-color: #1659de;
    color: white;
    border: none;
    padding: 10px 20px;
    border-radius: 4px;
    font-size: 1rem;
    font-weight: 600;
    cursor: pointer;
    transition: background-color 0.3s ease;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
    margin-left: 10px;

    &:hover {
      background-color: #003d8c;
    }
  }
`;

export const CancelButton = styled(Button)`
  && {
    background-color: #de1616;

    &:hover {
      background-color: #8c0000;
    }
  }
`;

export const Textarea = styled.textarea`
  && {
    width: 100%;
    padding: 0.5em;
    border: 1px solid #ccc;
    border-radius: 4px;
    box-sizing: border-box;
    font-size: 1em;
    resize: vertical;  // 세로로만 크기 조절 가능
    min-height: 100px; // 기본 높이 설정
  }
`;
