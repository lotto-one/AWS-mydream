package kr.ict.mydream.vo;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@Alias("chatroomvo")
public class ChatRoomVO {

    private int chtno;
    private int memno;
    private int cnsno;
    private String chatname;
    private Date credt;
    private Date upddt;

}
