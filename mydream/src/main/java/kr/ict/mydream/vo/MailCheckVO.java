package kr.ict.mydream.vo;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("cvo")
public class MailCheckVO {

    private String email;
    private String code;
    
}
