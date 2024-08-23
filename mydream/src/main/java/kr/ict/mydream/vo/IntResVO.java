package kr.ict.mydream.vo;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@Alias("intresvo")
public class IntResVO {
    private int intno;
    private int memno;
    private String inttypecd;
    private String aifeedbk;
    private String cnsfeedbk;
    private float cnsscore;
    private Date itenddt;
    private float reviewscore;
    private Date reviewdt;
    private Date credt;
    private Date upddt;
}
