package kr.ict.mydream.vo;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("cnscareervo")
public class CnsCareerVO {
    private int seqno;
    private int cnsno;
    private String term;
    private String content;
    private String detail;
    private String careerdiv;
    private Date credt;
    private Date upddt;
}
