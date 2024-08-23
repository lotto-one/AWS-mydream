package kr.ict.mydream.vo;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;
import java.util.Date;

@Getter
@Setter
@Alias("aiquestionvo")
public class AIQuestionVO {

    private int aiqno;
    private String aiquestion;
    private Date credt;
    private Date upddt;

}
