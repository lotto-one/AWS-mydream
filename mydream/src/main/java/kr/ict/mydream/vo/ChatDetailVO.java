package kr.ict.mydream.vo;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@Alias("chatdetailvo")
public class ChatDetailVO {
    private int chtno;
    private int seqno;
    private String chatdiv;
    private String content;
    private Date chatdt;
}
