package kr.co.ict.yourdream.consultantProfile;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class ConsultProfileDTO {
    private int cnsno;
    private String name;
    private String gendercd;
    private String categcd;
    private String email;
    private String phonenum;
    private String birthymd;
    private String introduce;
    private String quitymd;
    private String imgname;
    private String cnscareer;
    private String cnsproject;
    private Date credt;
    private Date upddt;
    private String password;
    private String rolecd;
    private List<CnsCareerDTO> cnscareerList;
}
