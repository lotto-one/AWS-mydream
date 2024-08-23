package kr.ict.mydream.member.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.ict.mydream.vo.MailCheckVO;

@RestController
@RequestMapping("/api/auth")
public class MailCertificationController {
    @Autowired
    private EmailSenderService emailSenderService;

    //이메일 중복확인

    @PostMapping("/emailCheck")
    public int sendEmail(@RequestBody MailCheckVO email){
        System.out.println("요청 처리됨" + email.getEmail());
        int checkEmail = emailSenderService.duplicateEmail(email.getEmail());
        if (checkEmail == 0) {
            emailSenderService.sendEmail(email.getEmail());
            return 0;
        } else {
            return 1;
        }
    }


        // 이메일 인증번호 확인
        @PostMapping("/emailCheck/certification")
        public boolean verifyCertificationNumber(@RequestBody MailCheckVO vo) {
            System.out.println(vo.getEmail() + ":" + vo.getCode());
            return emailSenderService.isVerify(vo.getEmail(), vo.getCode());
        }
}
