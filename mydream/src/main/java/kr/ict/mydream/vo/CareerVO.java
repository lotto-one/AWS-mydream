package kr.ict.mydream.vo;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("careervo")
public class CareerVO {
    private int rsmno;
    private int seqno;
    private String entymd;
    private String quitymd;
    private String compname;
    private String jobclass;
    private String maintask;
    private String jobposition;
    private Date credt;
    private Date upddt;
}
