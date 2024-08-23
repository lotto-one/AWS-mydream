package kr.ict.mydream.mainpage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kr.ict.mydream.vo.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mainpage")
public class MainPageController {

    @Autowired
    private MainPageService mainPageService;

    // 회원 정보 조회
    @GetMapping("/memberDetail")
    public ResponseEntity<MemberVO> getMemberDetail(@RequestParam("memno") int memno) {
        MemberVO member = mainPageService.getMemberDetail(memno);
        return ResponseEntity.ok(member);
    }

    // 스트레스율 조회(최근 면접 데이터 인성&직무)
    @GetMapping("/stressRate")
    public ResponseEntity<Float> getStressRate(@RequestParam("intno") int intno, @RequestParam("memno") int memno) {
        float stressRate = mainPageService.calculateStressRate(intno, memno);
        return ResponseEntity.ok(stressRate);
    }

    // 음성 점수 조회(최근 면접 데이터 인성&직무)
    @GetMapping("/voiceRate")
    public ResponseEntity<Float> getVoiceRate(@RequestParam("intno") int intno, @RequestParam("memno") int memno) {
        float voiceRate = mainPageService.calculateVoiceRate(intno, memno);
        return ResponseEntity.ok(voiceRate);
    }

    // 자세 점수 조회(최근 면접 데이터 인성&직무)
    @GetMapping("/postureRate")
    public ResponseEntity<Float> getPostureRate(@RequestParam("intno") int intno,
            @RequestParam("memno") int memno) {
        float postureRate = mainPageService.calculatePostureRate(intno, memno);
        return ResponseEntity.ok(postureRate);
    }

    // 컨설턴트 평가 점수 조회
    @GetMapping("/consultantScore")
    public ResponseEntity<IntResVO> getConsultantScore(
            @RequestParam("intno") int intno,
            @RequestParam("memno") int memno) {
        IntResVO consultantScore = mainPageService.getLatestConsultantScore(intno, memno);
        return ResponseEntity.ok(consultantScore);
    }

    // 회원-컨설턴트 매핑 및 컨설턴트 정보 조회
    @GetMapping("/memberConsultantDetail")
    public ResponseEntity<MemberConsultVO> getMemberConsultantDetail(@RequestParam("memno") int memno) {
        MemberConsultVO detail = mainPageService.getMemberConsultantDetail(memno);
        return detail != null ? ResponseEntity.ok(detail) : ResponseEntity.notFound().build();
    }

    // 회원 일정 조회
    @GetMapping("/memberSchedules")
    public ResponseEntity<List<ScheduleVO>> getSchedulesByMembers(@RequestParam("memno") int memno) {
        List<ScheduleVO> schedules = mainPageService.getSchedulesByMember(memno);
        return !schedules.isEmpty() ? ResponseEntity.ok(schedules) : ResponseEntity.notFound().build();
    }

    // 컨설턴트 직무 질문&답변 STT
    @GetMapping("/consultantQuestions")
    public List<IntDetailVO> getConsultantQuestions(
            @RequestParam("intno") int intno,
            @RequestParam(value = "qnos", required = false) List<Integer> qnos) {
        if (qnos == null || qnos.isEmpty()) {
            qnos = Arrays.asList(6, 7); // 기본값 설정
        }
        return mainPageService.getConsultantQuestions(intno, qnos);
    }

    // 컨설턴트 직무 답변 피드백
    @GetMapping("/consultantFeedback")
    public List<ConsultevalVO> getConsultantFeedback(
            @RequestParam("memno") int memno,
            @RequestParam("cnsno") int cnsno,
            @RequestParam("intno") int intno,
            @RequestParam(value = "qnos", required = false) List<Integer> qnos) {
        if (qnos == null || qnos.isEmpty()) {
            qnos = Arrays.asList(6, 7); // 기본값 설정
        }
        return mainPageService.getConsultantFeedback(memno, cnsno, intno, qnos);
    }

    // 컨설턴트 피드백(총평)
    @GetMapping("/consultantTotalFeedback")
    public String getConsultantTotalFeedback(@RequestParam("memno") int memno, @RequestParam("intno") int intno) {
        return mainPageService.getConsultantTotalFeedback(memno, intno);
    }

    // 최근 5개의 면접 데이터
    @GetMapping("/recentInterviewScores")
    public ResponseEntity<Map<String, Object>> getRecentInterviewData(@RequestParam("memno") int memno) {
        Map<String, Object> interviewData = mainPageService.getRecentInterviewData(memno);
        return ResponseEntity.ok(interviewData);
    }

    // 회원의 최근 인터뷰 정보를 가져옴
    @GetMapping("/latestInterviewInfo")
    public ResponseEntity<Map<String, Integer>> getLatestInterviewInfo(@RequestParam("memno") int memno) {
        Map<String, Integer> latestInfo = mainPageService.getLatestInterviewInfo(memno);
        if (latestInfo == null || latestInfo.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(latestInfo);
    }
}