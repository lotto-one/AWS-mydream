package kr.co.ict.yourdream.vo;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class LoginVO {
    
    private int logno;
    private int memno;
    private Date logindt;
    private Date logoutdt;
}
