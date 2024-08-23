package kr.co.ict.yourdream.consultantQuestion;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/consultant/CTQ")
public class CTQuestionController {
    @Autowired
    private CTQuestionService ctQuestionService;

    @PostMapping("/addQuestion")
    public ResponseEntity<?> addQuestion(@RequestBody CTQuestionDTO dto) {
        ctQuestionService.create(dto);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/updateQuestion")
    public ResponseEntity<?> updateQuestion(@RequestBody CTQuestionDTO dto) {
        ctQuestionService.update(dto);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/CTQuestionList")
    public List<CTQuestionDTO> ctQuestionList(@RequestParam("cnsno") Long cnsno) {
        return ctQuestionService.list(cnsno);
    }

    @GetMapping("/CTQuestionDel")
    public ResponseEntity<?> ctQuestionDel(@RequestParam("cnsqno") Long cnsqno) {
        ctQuestionService.delete(cnsqno);
        return ResponseEntity.ok().body(cnsqno + "번째 데이터 삭제");
    }

}
