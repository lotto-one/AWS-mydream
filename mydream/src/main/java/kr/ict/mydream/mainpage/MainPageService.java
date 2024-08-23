package kr.ict.mydream.mainpage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.ict.mydream.vo.*;
import java.util.Date;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MainPageService {

        @Autowired
        private MainPageDao mainPageDao;

        // 회원 정보 조회
        public MemberVO getMemberDetail(int memno) {
                return mainPageDao.getMemberDetail(memno);
        }

        // 회원-컨설턴트 매핑 및 컨설턴트 정보 조회
        public MemberConsultVO getMemberConsultantDetail(int memno) {
                MemberConsultVO mapping = mainPageDao.getMemberConsultantDetail(memno);
                return mapping;
        }

        // 감정 점수
        public int calculateStressRate(int intno, int memno) {
                Map<String, Object> params = new HashMap<>();
                params.put("intno", intno);
                params.put("memno", memno);

                List<IntDetailVO> latestDetails = mainPageDao.getLatestEmotionDetails(params);
                if (latestDetails.isEmpty()) {
                        return 0; // 데이터가 없으면 0점 반환
                }

                float totalEmotionScore = 0;
                for (IntDetailVO detail : latestDetails) {
                        totalEmotionScore += detail.getEscore();
                }

                float avgEscore = totalEmotionScore / latestDetails.size();
                return Math.round(Math.max(avgEscore, 0)); // 음수가 나오지 않도록 보정
        }

        // 음성 점수
        public int calculateVoiceRate(int intno, int memno) {
                Map<String, Object> params = new HashMap<>();
                params.put("intno", intno);
                params.put("memno", memno);

                List<IntDetailVO> latestDetails = mainPageDao.getLatestVoiceDetails(params);
                if (latestDetails.isEmpty()) {
                        return 0; // 데이터가 없으면 0점 반환
                }

                float totalVoiceScore = 0;
                for (IntDetailVO detail : latestDetails) {
                        totalVoiceScore += detail.getVscore();
                }

                float avgVscore = totalVoiceScore / latestDetails.size();
                return Math.round(Math.max(avgVscore, 0)); // 음수가 나오지 않도록 보정
        }

        // 자세 점수
        public int calculatePostureRate(int intno, int memno) {
                Map<String, Object> params = new HashMap<>();
                params.put("intno", intno);
                params.put("memno", memno);

                List<IntDetailVO> latestDetails = mainPageDao.getLatestPostureDetails(params);
                if (latestDetails.isEmpty()) {
                        return 0; // 데이터가 없으면 0점 반환
                }

                float totalPostureScore = 0;
                for (IntDetailVO detail : latestDetails) {
                        totalPostureScore += detail.getPscore();
                }

                float avgPscore = totalPostureScore / latestDetails.size();
                return Math.round(Math.max(avgPscore, 0)); // 음수가 나오지 않도록 보정
        }

        // 컨설턴트 평가 점수 조회
        public IntResVO getLatestConsultantScore(int intno, int memno) {
                Map<String, Object> params = new HashMap<>();
                params.put("intno", intno);
                params.put("memno", memno);
                return mainPageDao.getLatestConsultantScore(params);
        }

        // 회원 일정 조회
        public List<ScheduleVO> getSchedulesByMember(int memno) {
                return mainPageDao.getSchedulesByMember(memno);
        }

        // 컨설턴트 직무 질문& 답변 STT
        public List<IntDetailVO> getConsultantQuestions(int intno, List<Integer> qnos) {
                Map<String, Object> params = new HashMap<>();
                params.put("intno", intno);
                params.put("qnos", qnos);
                return mainPageDao.getConsultantQuestions(params);
        }

        // 컨설턴트 직무 질문&답변 피드백
        public List<ConsultevalVO> getConsultantFeedback(int memno, int cnsno, int intno, List<Integer> qnos) {
                Map<String, Object> params = new HashMap<>();
                params.put("memno", memno);
                params.put("cnsno", cnsno);
                params.put("intno", intno);
                params.put("qnos", qnos);
                return mainPageDao.getConsultantFeedback(params);
        }

        // 컨설턴트 피드백(총평)
        public String getConsultantTotalFeedback(int memno, int intno) {
                Map<String, Object> params = new HashMap<>();
                params.put("memno", memno);
                params.put("intno", intno);

                IntResVO result = mainPageDao.getConsultantTotalFeedback(params);
                return result != null ? result.getCnsfeedbk() : null;
        }

        // 최근 5개의 면접 데이터
        public Map<String, Object> getRecentInterviewData(int memno) {
                List<IntDetailVO> interviewDetails = mainPageDao.getInterviewDetails(memno);

                Map<String, Object> result = new HashMap<>();
                result.put("interviewData", new ArrayList<Map<String, Object>>());

                Map<Integer, Map<String, List<Map<String, Object>>>> questionData = new HashMap<>();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                for (IntDetailVO detail : interviewDetails) {
                        int intno = detail.getIntno();

                        // 면접 데이터 처리
                        if (!questionData.containsKey(intno)) {
                                Map<String, Object> interviewInfo = new HashMap<>();
                                interviewInfo.put("intno", intno);
                                interviewInfo.put("date", sdf.format(detail.getCredt()));
                                ((List<Map<String, Object>>) result.get("interviewData")).add(interviewInfo);

                                questionData.put(intno, new HashMap<>());
                        }

                        // 질문별 데이터 처리
                        String qKey = "Q" + detail.getQno();
                        Map<String, List<Map<String, Object>>> interviewQuestions = questionData.get(intno);

                        if (!interviewQuestions.containsKey(qKey)) {
                                interviewQuestions.put(qKey, new ArrayList<>());
                        }

                        Map<String, Object> questionInfo = new HashMap<>();
                        questionInfo.put("voiceScore", detail.getVscore());
                        questionInfo.put("emotionScore", detail.getEscore()); // escore를 그대로 사용
                        questionInfo.put("postureScore", detail.getPscore());
                        questionInfo.put("ecntgood", detail.getEcntgood());
                        questionInfo.put("ecntsoso", detail.getEcntsoso());
                        questionInfo.put("ecntbad", detail.getEcntbad());

                        interviewQuestions.get(qKey).add(questionInfo);
                }

                result.put("questionData", questionData);

                return result;
        }

        // 회원의 최근 인터뷰 정보를 가져옴
        public Map<String, Integer> getLatestInterviewInfo(int memno) {
                Map<String, Object> result = mainPageDao.getLatestInterviewInfo(memno);
                if (result == null || result.isEmpty()) {
                        return null;
                }

                Map<String, Integer> convertedResult = new HashMap<>();
                for (Map.Entry<String, Object> entry : result.entrySet()) {
                        if (entry.getValue() != null) {
                                if (entry.getValue() instanceof Integer) {
                                        convertedResult.put(entry.getKey(), (Integer) entry.getValue());
                                } else if (entry.getValue() instanceof BigDecimal) {
                                        convertedResult.put(entry.getKey(), ((BigDecimal) entry.getValue()).intValue());
                                } else {
                                        // 예외 처리 또는 로깅
                                        System.out.println("Unexpected type for key " + entry.getKey() + ": "
                                                        + entry.getValue().getClass());
                                }
                        }
                }
                return convertedResult;
        }

}