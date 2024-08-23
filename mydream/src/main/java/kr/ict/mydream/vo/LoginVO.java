package kr.ict.mydream.vo;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@Alias("loginvo")
public class LoginVO {
    private int logno;
    private int memno;
    private Date logindt;
    private Date logoutdt;
}
