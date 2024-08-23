package kr.ict.mydream.vo;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("intfeedbkvo")
public class IntFeedbkVO {
    private int feedno;
    private int intno;
    private String efeed1;
    private String efeed2;
    private String pfeed1;
    private String pfeed2;
    private String vfeed1;
    private String vfeed2;
    private String sttfeed1;
    private String sttfeed2;
}
