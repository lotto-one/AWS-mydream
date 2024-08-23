package kr.co.ict.yourdream.feedback;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    public List<FeedbackDTO> feedbkList(Integer memno) {
        List<FeedbackDTO> intResVOList = feedbackRepository.findByMemnoAndInttypecd(memno);
        return intResVOList;
    }

    public List<MemConsultDTO> feedbkMain(Integer cnsno) {
        List<MemConsultDTO> memberConsultVOList = feedbackRepository.findByMemnoAndUseyn(cnsno);
        return memberConsultVOList;
    }

    public FeedbackDetail_InfoDTO feedInfo(Integer intno) {
        FeedbackDetail_InfoDTO feedbkInfo = feedbackRepository.fInfoDTO(intno);
        return feedbkInfo;
    }

    public FeedbackDetail_InFeedbkDTO feedResEval(Integer intno) {
        FeedbackDetail_InFeedbkDTO feedResEval = feedbackRepository.fResEvalDTO(intno);
        return feedResEval;
    }

    public FeedbackDetail_IntResDTO feedConEval(Integer intno) {
        FeedbackDetail_IntResDTO feedConEval = feedbackRepository.fResConEvalDTO(intno);
        return feedConEval;
    }

    public List<FeedbackDetail_IntDetailDTO> feedEscore(Integer intno) {
        List<FeedbackDetail_IntDetailDTO> feedEscore = feedbackRepository.fEscoreDTO(intno);
        return feedEscore;
    }

    public List<FeedbackDetail_ConsultEvalDTO> feedQconEval(Integer intno) {
        List<FeedbackDetail_ConsultEvalDTO> feedQconEval = feedbackRepository.fQconEvalDTO(intno);
        return feedQconEval;
    }

    public void feedQConUpdate(Map<String, Object> data) {
        String qcnsfeedbk = (String) data.get("qcnsfeedbk");
        Integer intno = Integer.parseInt((String) data.get("intno"));
        Integer qno = (Integer) data.get("qno");
        feedbackRepository.fQfeedUpdate(qcnsfeedbk, intno, qno);
    }

    public void feedCnsfeedbkUpdate(Map<String, Object> data) {
        String cnsfeedbk = (String) data.get("cnsfeedbk");
        Integer intno = Integer.parseInt((String) data.get("intno"));
        feedbackRepository.fCnsfeedbkUpdate(cnsfeedbk, intno);
    }

    public ResumeInfoDTO resumeInfo(Integer memno) {
        return feedbackRepository.resumeInfo(memno);
    }

    public List<ResumeEduDTO> resumeEdu(Integer rsmno) {
        return feedbackRepository.resumeEdu(rsmno);
    }

    public List<ResumeCarDTO> resumeCar(Integer rsmno) {
        return feedbackRepository.resumeCar(rsmno);
    }

    public List<ResumeSelfDTO> resumeSelf(Integer rsmno) {
        return feedbackRepository.resumeSelf(rsmno);
    }
}
