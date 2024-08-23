package kr.co.ict.yourdream.login.api;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsultDTO {
    private String name;
    private String gendercd;
    private String categcd;
    private String email;
    private String phonenum;
    private String birthymd;
    private String introduce;
    private String imgname;
    private String cnscareer;
    private String cnsproject;
    private String password;
    private String rolecd;
    // private String term;
    // private String content;    
    // private String detail;
    // private String careerdiv;
    private List<CareerDTObok> cnscareer_vo; // Career 정보를 리스트로 추가
    public ConsultDTO() {
        this.password = "11"; // 기본값 설정
        this.rolecd = "C"; // 기본값 설정
    }
}
