package kr.ict.mydream.myconsultant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.ict.mydream.vo.ConsultVO;
import kr.ict.mydream.vo.MemberConsultVO;
import kr.ict.mydream.vo.MemberVO;

@RestController
@RequestMapping("/myconsult")
public class MyConsultController {

    @Autowired
    private MyConsultService myConsultService;

    @PostMapping("/mycon")
    public ConsultVO myconsultprofile(@RequestBody MemberVO vo) {
        ConsultVO cvo = new ConsultVO();
        MemberConsultVO memberConsultVO = myConsultService.findCon(vo.getMemno());
        try {
            if ("Y".equals(memberConsultVO.getUseyn())) {
                System.out.println(memberConsultVO.getCnsno());
                cvo = myConsultService.getConsultDetails(memberConsultVO.getCnsno());
                return cvo;
            } else if (memberConsultVO.getUseyn() == "N") {
    
            } else {
                System.out.println("잘못된 번호");
                return null;
            }
        } catch (Exception e) {
            return null;
        }
        


        return cvo;
    }
}
