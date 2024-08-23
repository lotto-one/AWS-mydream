package kr.ict.mydream.member.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.Random;


@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailCertificationDAO emailCertificationDAO;

    @Autowired
    private CertificationNumberDAO certificationNumberDAO;

    private String authCode;

    public int duplicateEmail(String email) {
        long checkEmail = emailCertificationDAO.countByEmail(email);
        return checkEmail > 0 ? 1 : 0;
    }

    public void createAuthCode() {
        int length = 6;
        StringBuilder authCode = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int type = random.nextInt(3);

            switch (type) {
                case 0:
                    authCode.append(random.nextInt(10));
                    break;
                case 1:
                    authCode.append((char) (random.nextInt(26) + 65));
                    break;
                case 2:
                    authCode.append((char) (random.nextInt(26) + 97));
                    break;
            }
        }
        this.authCode = authCode.toString();
    }

    public void sendEmail(String toEmail) {
        createAuthCode();
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("ictedu07@naver.com");
            helper.setTo(toEmail);
            helper.setSubject("ICTStudy의 X팀의 회원가입 인증번호 발송");

            String body = "<html>" +
                    "<body>" +
                    "<h1>ICTStudy의 X팀의 회원가입을 위한 인증번호</h1>" +
                    "<p>회원가입을 완료하기 위해 아래의 인증코드를 입력해주세요.</p>" +
                    "<p>인증코드: <strong>" + authCode + "</strong></p>" +
                    "</body>" +
                    "</html>";

            helper.setText(body, true);
            mailSender.send(message);
            certificationNumberDAO.saveCertificationNumber(toEmail, authCode);
            System.out.println("인증번호:"+authCode);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isVerify(String email, String authCode) {
        System.out.println(certificationNumberDAO.hasKey(email));
        System.out.println(authCode);
        System.out.println(certificationNumberDAO.getCertificationNumber(email));
        if (certificationNumberDAO.hasKey(email) && certificationNumberDAO
                .getCertificationNumber(email).equals(authCode)) {
            certificationNumberDAO.deleteCertificationNumber(email);
            return true;
        } else {
            return false;
        }
    }
    
    
}
