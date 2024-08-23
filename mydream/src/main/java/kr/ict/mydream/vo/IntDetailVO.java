package kr.ict.mydream.vo;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@Alias("intdetailvo")
public class IntDetailVO {
    private int intno;
    private int qno;
    private int aiqno;
    private int cnsqno;
    private String question;
    private String answer;
    private float ecntgood;
    private float ecntsoso;
    private float ecntbad;
    private float pbadcnt;
    private float vhertz;
    private float vjitter;
    private float vspeed;
    private String aifeedbk;
    private Date credt;
    private Date upedt;
    private float escore;
    private float pscore;
    private float vscore;
}
