package kr.ict.mydream.consultantchoice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.ict.mydream.vo.ConsultVO;

@RestController
@RequestMapping("/consultantInfo")
public class ConsultantChoiceController {

    @Autowired
    private ConsultantChoiceService consultantChoiceService;

    @PostMapping("/consultantList")
    public List<ConsultVO> consultantList() {
        return consultantChoiceService.consultantList();
    };

    @PostMapping("/consultantUse")
    public Map<String, Object> consultantUse(@RequestBody Map<String, Integer> map) {
        int memno = map.get("memno");
        Map<String, Object> res = consultantChoiceService.consultantUse(memno);
        System.out.println(res);
        if (res == null) {
            Map<String, Object> res2 = new HashMap<>();
            res2.put("CNT", 0);
            res2.put("CNSNO", null);
            System.out.println(res2);
            return res2;
        } else {
            System.out.println(res);
            return res;
        }
    }

    @PostMapping("/consultantAppl")
    public void consultantAppl(@RequestBody Map<String, Integer> data) {
        // 컨설턴트 신청 로그 유무 확인
        int history = consultantChoiceService.consultantHistyory(data);

        if (history == 0) {
            consultantChoiceService.consultantAppl(data);
            consultantChoiceService.consultantChatCreate(data); // 챗룸 생성
        } else {
            consultantChoiceService.consultantApplUp(data);
            consultantChoiceService.consultantChatCreate(data); // 챗룸 생성
        }
    }

    @PostMapping("/consultantCancle")
    public void consultantCancle(@RequestBody Map<String, Integer> map) {
        int memno = map.get("memno");
        consultantChoiceService.consultantCancle(memno);
    }
}
