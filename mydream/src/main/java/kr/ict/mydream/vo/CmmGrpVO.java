package kr.ict.mydream.vo;

import org.apache.ibatis.type.Alias;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("cmmgrpvo")
public class CmmGrpVO {
    private String cmmgrpcd;
    private String cmmgrpname;
    private String useyn;
    private Date credt;
    private Date upddt;
}
