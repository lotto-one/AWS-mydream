package kr.co.ict.yourdream.feedback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consultant/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/main")
    public List<MemConsultDTO> feedbkMain(@RequestBody Map<String, Integer> map) {
        Integer cnsno = (int) map.get("cnsno");
        return feedbackService.feedbkMain(cnsno);
    }

    @PostMapping("/feedbkList")
    public List<FeedbackDTO> feedbkList(@RequestBody Map<String, Integer> map) {
        Integer memno = (int) map.get("memno");
        return feedbackService.feedbkList(memno);
    }

    @GetMapping("/feedbkDetailInfo")
    public Map<String, Object> feedbkDetail(@RequestParam("intno") Integer intno) {
        Map<String, Object> res = new HashMap<>();
        res.put("info", feedbackService.feedInfo(intno));
        return res;
    }

    @GetMapping("/feedbkDetailResEval")
    public Map<String, Object> feedbkDetailResEval(@RequestParam("intno") Integer intno) {
        Map<String, Object> res = new HashMap<>();
        res.put("resEval", feedbackService.feedResEval(intno));
        return res;
    }

    @GetMapping("/feedbkDetailResConEval")
    public Map<String, Object> feedbkDetailResConEval(@RequestParam("intno") Integer intno) {
        Map<String, Object> res = new HashMap<>();
        res.put("resConEval", feedbackService.feedConEval(intno));
        return res;
    }

    @GetMapping("/feedbkScore")
    public Map<String, Object> feedbkScore(@RequestParam("intno") Integer intno) {
        Map<String, Object> res = new HashMap<>();
        res.put("escore", feedbackService.feedEscore(intno));
        return res;
    }

    @GetMapping("/feedbkDetailQconEval")
    public Map<String, Object> feedbkDetailQconEval(@RequestParam("intno") Integer intno) {
        Map<String, Object> res = new HashMap<>();
        res.put("QconEval", feedbackService.feedQconEval(intno));
        return res;
    }

    @PostMapping("/feedbkQUpdate")
    public void feedbkQUpdate(@RequestBody Map<String, Object> data) {
        feedbackService.feedQConUpdate(data);
    }

    @PostMapping("/feedbkCnsUpdate")
    public void feedbkCnsUpdate(@RequestBody Map<String, Object> data) {
        feedbackService.feedCnsfeedbkUpdate(data);
    }

    @GetMapping("/resumeInfo")
    public ResumeInfoDTO resumeInfo(@RequestParam("memno") Integer memno) {
        return feedbackService.resumeInfo(memno);
    }

    @PostMapping("/resumeEdu")
    public List<ResumeEduDTO> resumeEdu(@RequestBody Map<String, Integer> data) {
        Integer rsmno = data.get("rsmno");
        return feedbackService.resumeEdu(rsmno);
    }

    @PostMapping("/resumeCar")
    public List<ResumeCarDTO> resumeCar(@RequestBody Map<String, Integer> data) {
        Integer rsmno = data.get("rsmno");
        return feedbackService.resumeCar(rsmno);
    }

    @PostMapping("/resumeSelf")
    public List<ResumeSelfDTO> resumeSelf(@RequestBody Map<String, Integer> data) {
        Integer rsmno = data.get("rsmno");
        return feedbackService.resumeSelf(rsmno);
    }
}
