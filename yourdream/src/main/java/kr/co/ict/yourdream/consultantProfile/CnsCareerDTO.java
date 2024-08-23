package kr.co.ict.yourdream.consultantProfile;

import lombok.Data;
import java.util.Date;

@Data
public class CnsCareerDTO {
    private int cnsno;
    private int seqno;
    private String term;
    private String content;
    private String detail;
    private String careerdiv;
    private Date credt;
    private Date upddt;
}
