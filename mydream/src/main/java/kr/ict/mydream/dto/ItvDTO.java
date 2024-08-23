package kr.ict.mydream.dto;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("itvdto")
public class ItvDTO {

    public ItvDTO() {
    }

    public ItvDTO(String mname, Date credt, String cnsfeedbk, String efeed1, String efeed2, String pfeed1,
            String pfeed2, String vfeed1, String vfeed2, String sttfeed1, String sttfeed2, String question, int intno,
            double reviewscore) {
        this.mname = mname;
        this.credt = credt;
        this.cnsfeedbk = cnsfeedbk;
        this.efeed1 = efeed1;
        this.efeed2 = efeed2;
        this.pfeed1 = pfeed1;
        this.pfeed2 = pfeed2;
        this.vfeed1 = vfeed1;
        this.vfeed2 = vfeed2;
        this.sttfeed1 = sttfeed1;
        this.sttfeed2 = sttfeed2;
        this.question = question;
        this.intno = intno;
        this.reviewscore = reviewscore;
    }

    private String mname;
    private Date credt;
    private String cnsfeedbk;
    private String efeed1;
    private String efeed2;
    private String pfeed1;
    private String pfeed2;
    private String vfeed1;
    private String vfeed2;
    private String sttfeed1;
    private String sttfeed2;
    private String question;
    private int intno;
    private double reviewscore;

}
