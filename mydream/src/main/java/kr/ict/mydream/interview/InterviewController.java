package kr.ict.mydream.interview;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.ict.mydream.vo.IntDetailVO;
import kr.ict.mydream.vo.IntFeedbkVO;
import kr.ict.mydream.vo.IntResVO;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/interview")
public class InterviewController {

    @Autowired
    private InterviewService interviewService;

    @GetMapping("/getname")
    public String getName(@RequestParam("memno") int memno) {
        String name = interviewService.getName(memno);
        return name;
    }

    @GetMapping("/getcnsYnN")
    public String getCnsYnN(@RequestParam("memno") int memno) {
        String cnsYnN = interviewService.getCnsYnN(memno);

        return cnsYnN;
    }

    @GetMapping("/getquestion")
    public List<Map> getquestion(@RequestParam("memno") int memno) {
        List<Map> question = interviewService.getQuestion(memno);
        System.out.println("controller에서question" + question);
        return question;
    }

    @GetMapping("/getinttypename")
    public String getIntTypeName(@RequestParam("inttypecd") int inttypecd) {
        String inttypename = interviewService.getIntTypeName(inttypecd);
        return inttypename;
    }

    @PostMapping("/setintres")
    public int setIntRes(@RequestBody IntResVO intresvo) {
        System.out.println("intresvo.getInttypecd => " + intresvo.getInttypecd());
        System.out.println("intresvo.getMemno => " + intresvo.getMemno());
        interviewService.setIntRes(intresvo);
        System.out.println(intresvo.getIntno());
        int intno = intresvo.getIntno();
        return intno;
    }

    @PostMapping("/setintdetail")
    public void setIntDetail(@RequestBody List<IntDetailVO> detailvoList) {
        // for (int i = 0; i <= 6; i++) {

        // System.out.println(i + 1 + "번쨰 intno" + detailvoList.get(i).getIntno());
        // System.out.println(i + 1 + "번쨰 qno" + detailvoList.get(i).getQno());
        // System.out.println(i + 1 + "번쨰 aiqno" + detailvoList.get(i).getAiqno());
        // System.out.println(i + 1 + "번쨰 cnsqno" + detailvoList.get(i).getCnsqno());
        // System.out.println(i + 1 + "번쨰 question" +
        // detailvoList.get(i).getQuestion());
        // System.out.println(i + 1 + "번쨰 answer" + detailvoList.get(i).getAnswer());
        // System.out.println(i + 1 + "번쨰 ecntgood" +
        // detailvoList.get(i).getEcntgood());
        // System.out.println(i + 1 + "번쨰 ecntsoso" +
        // detailvoList.get(i).getEcntsoso());
        // System.out.println(i + 1 + "번쨰 ecntbad" + detailvoList.get(i).getEcntbad());
        // System.out.println(i + 1 + "번쨰 pbadcnt" + detailvoList.get(i).getPbadcnt());
        // System.out.println(i + 1 + "번쨰 vhertz" + detailvoList.get(i).getVhertz());
        // System.out.println(i + 1 + "번쨰 vjitter" + detailvoList.get(i).getVjitter());
        // System.out.println(i + 1 + "번쨰 vspeed" + detailvoList.get(i).getVspeed());
        // System.out.println(i + 1 + "번쨰 aifeedbk" +
        // detailvoList.get(i).getAifeedbk());
        // System.out.println(i + 1 + "번쨰 escore" + detailvoList.get(i).getEscore());
        // System.out.println(i + 1 + "번쨰 pscore" + detailvoList.get(i).getPscore());
        // System.out.println(i + 1 + "번쨰 vscore" + detailvoList.get(i).getVscore());
        // }
        interviewService.setIntDetail(detailvoList);
    }

    @PostMapping("/setintfeedbk")
    public void setIntFeedbk(@RequestBody IntFeedbkVO intfeedbkvo) {
        System.out.println("setintfeedbk왔다");
        System.out.println("getIntno=>" + intfeedbkvo.getIntno());
        System.out.println("getEfeed1=>" + intfeedbkvo.getEfeed1());
        System.out.println("getEfeed2=>" + intfeedbkvo.getEfeed2());
        System.out.println("getPfeed1=>" + intfeedbkvo.getPfeed1());
        System.out.println("getPfeed2=>" + intfeedbkvo.getPfeed2());
        System.out.println("getVfeed1=>" + intfeedbkvo.getVfeed1());
        System.out.println("getVfeed2=>" + intfeedbkvo.getVfeed2());
        System.out.println("getSttfeed1=>" + intfeedbkvo.getSttfeed1());
        System.out.println("getSttfeed2=>" + intfeedbkvo.getSttfeed2());
        interviewService.setIntFeedbk(intfeedbkvo);
    }

    @GetMapping("/getresnametime")
    public Map getResNameTime(@RequestParam("memno") int memno) {
        return interviewService.getResNameTime(memno);
    }

}
