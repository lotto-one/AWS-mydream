package kr.co.ict.yourdream.consultantDashBoard;

import lombok.Data;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ConsultantDTO {
    private int cnsno;
    private String name;
    private String gendercd;
    private String categcd;
    private String email;
    private String password;
    private String phonenum;
    private String birthymd;
    private String introduce;
    private String quitymd;
    private String imgname;
    private String cnscareer;
    private String cnsproject;
    private String rolecd;
    private Date credt;
    private Date upddt;
}
