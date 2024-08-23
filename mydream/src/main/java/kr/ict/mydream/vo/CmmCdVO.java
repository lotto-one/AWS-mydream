package kr.ict.mydream.vo;

import org.apache.ibatis.type.Alias;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("cmmcdvo")
public class CmmCdVO {
    private String cmmcd;
    private String cmmgrpcd;
    private String cmmcdname;
    private String useyn;
    private Date credt;
    private Date upddt;

}
