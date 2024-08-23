import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './Consult_Question.css';

const ConsultQuestion: React.FC = () => {
  const [showForm, setShowForm] = useState(false);
  const [questions, setQuestions] = useState<{ question: string, keywords: string[], cnsqno: number }[]>([]);
  const [currentQuestion, setCurrentQuestion] = useState('');
  const [currentKeywords, setCurrentKeywords] = useState(['', '', '']);
  const [editingQuestion, setEditingQuestion] = useState<{ question: string, keywords: string[], cnsqno: number } | null>(null);

  useEffect(() => {
    fetchQuestions();
  }, []);

  const fetchQuestions = async () => {
    try {
      const cnsno = localStorage.getItem('cnsno'); // localStorage에서 cnsno 값 가져오기
      if (!cnsno) {
        console.error("cnsno is not found in localStorage");
        return;
      }

      const response = await axios.get(`${process.env.REACT_APP_BACK_END_URL}/consultant/CTQ/CTQuestionList`, {
        params: { cnsno }
      });

      const fetchedQuestions = response.data.map((item: any) => ({
        question: item.question,
        keywords: [item.keyword1, item.keyword2, item.keyword3].filter((k: string) => k),
        cnsqno: item.cnsqno,
      }));
      setQuestions(fetchedQuestions);
    } catch (error) {
      console.error("There was an error fetching the data!", error);
    }
  };

  const del = async (cnsqno: number) => {
    try {
      await axios.get(`${process.env.REACT_APP_BACK_END_URL}/consultant/CTQ/CTQuestionDel`, {
        params: { cnsqno }
      });
      fetchQuestions();
    } catch (error) {
      console.error("There was an error deleting the question!", error);
    }
  };

  const handleSaveQuestion = async () => {
    if (!currentQuestion || !currentKeywords.some(keyword => keyword)) {
      alert('질문과 최소한 하나의 키워드는 필수입니다.');
      return;
    }

    if (!editingQuestion && questions.length >= 5) {
      alert('질문은 최대 5개까지 등록할 수 있습니다.');
      return;
    }

    try {
      const cnsno = localStorage.getItem('cnsno');;

      if (editingQuestion) {
        // 수정 모드
        await axios.post(`${process.env.REACT_APP_BACK_END_URL}/consultant/CTQ/updateQuestion`, {
          cnsqno: editingQuestion.cnsqno,
          cnsno,
          question: currentQuestion,
          keyword1: currentKeywords[0],
          keyword2: currentKeywords[1],
          keyword3: currentKeywords[2],
        });
      } else {
        // 추가 모드
        await axios.post(`${process.env.REACT_APP_BACK_END_URL}/consultant/CTQ/addQuestion`, {
          cnsno,
          question: currentQuestion,
          keyword1: currentKeywords[0],
          keyword2: currentKeywords[1],
          keyword3: currentKeywords[2],
        });
      }

      fetchQuestions();
      setCurrentQuestion('');
      setCurrentKeywords(['', '', '']);
      setShowForm(false);
      setEditingQuestion(null);
    } catch (error) {
      console.error("There was an error saving the question!", error);
    }
  };

  const handleEditClick = (question: string, keywords: string[], cnsqno: number) => {
    window.scrollTo(0, 0);
    setCurrentQuestion(question);
    setCurrentKeywords(keywords);
    setEditingQuestion({ question, keywords, cnsqno });
    setShowForm(true);
  };

  return (
    <div className="container question-container">
      <div className='question-title'>
        <h2>{editingQuestion ? '질문 수정하기' : '질문 등록하기'}</h2>
      </div>
      {showForm && (
        <div className="question-input">
          <input 
            placeholder='질문을 입력하세요'
            className="form-control form-control-lg question-q" 
            type="text"             
            value={currentQuestion}
            onChange={(e) => setCurrentQuestion(e.target.value)}
          />       
          {currentKeywords.map((keyword, index) => (
            <input 
              key={index}
              type='text' 
              className='form-control form-control-sm question-key'
              placeholder='키워드를 입력하세요'
              value={keyword}
              onChange={(e) => {
                const newKeywords = [...currentKeywords];
                newKeywords[index] = e.target.value;
                setCurrentKeywords(newKeywords);
              }}
            />
          ))}
          <div className='question-button'>
            <button className='btn btn-pre' onClick={handleSaveQuestion}>
              {editingQuestion ? '수정' : '등록'}
            </button>
          </div>
        </div>
      )}
      {!showForm && (
        <div className='question-button'>
          <button className='btn btn-pre' onClick={() => setShowForm(true)}>질문 추가</button>
        </div>
      )}
      <br/>
      <div className='question-list-title'>
        <div className='question-list-left'>
          <p>질문리스트</p>
        </div>
        <div className='question-list-right'>
          <p>키워드</p>
        </div>
        <div className='ctq-btnarea'>
          <p>수정/삭제</p>
        </div>
      </div>
      {questions.map((item, index) => (
        <div className='question-list' key={index}>
          <div className='question-list-left'>
            <h1>Q{index + 1}.<br/></h1> {item.question}
          </div>
          <div className='question-list-right'>
            {item.keywords.join(', ')}
          </div>
          <div className='up_del_btn'>
            <button className='ctq_up_btn' onClick={() => handleEditClick(item.question, item.keywords, item.cnsqno)}>수정</button>
            <button className='ctq_del_btn' onClick={() => del(item.cnsqno)}>삭제</button>
          </div>
        </div>
      ))}      
    </div>
  );
};

export default ConsultQuestion;
