package kr.ict.mydream.mainpage;

import org.apache.ibatis.annotations.Mapper;
import kr.ict.mydream.vo.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface MainPageDao {
    // 회원 정보 조회
    MemberVO getMemberDetail(int memno);

    // 회원-컨설턴트 매핑 및 컨설턴트 정보 조회
    MemberConsultVO getMemberConsultantDetail(int memno);

    // 회원 면접 결과(최신 감정 데이터 조회)
    List<IntDetailVO> getLatestEmotionDetails(Map<String, Object> params);

    // 회원 면접 결과(최신 음성 데이터 조회)
    List<IntDetailVO> getLatestVoiceDetails(Map<String, Object> params);

    // 회원 면접 결과(최신 자세 데이터 조회)
    List<IntDetailVO> getLatestPostureDetails(Map<String, Object> params);

    // 회원 면접 결과(최신 컨설턴트 평가 점수 조회)
    IntResVO getLatestConsultantScore(Map<String, Object> params);

    // 회원 일정 조회
    List<ScheduleVO> getSchedulesByMember(int memno);

    // 컨설턴트 직무 질문&답변 STT
    List<IntDetailVO> getConsultantQuestions(Map<String, Object> params);

    // 컨설턴트 직무 답변 피드백
    List<ConsultevalVO> getConsultantFeedback(Map<String, Object> params);

    // 컨설턴트 피드백(총평)
    IntResVO getConsultantTotalFeedback(Map<String, Object> params);

    // 최근 5개의 인성면접 데이터
    List<IntDetailVO> getInterviewDetails(int memno);

    // 회원의 최근 인터뷰 정보를 가져옴
    Map<String, Object> getLatestInterviewInfo(int memno);
}