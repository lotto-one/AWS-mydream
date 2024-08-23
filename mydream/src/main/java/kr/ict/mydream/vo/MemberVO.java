package kr.ict.mydream.vo;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@Alias("membervo")
public class MemberVO {
    private int memno;
    private String name;
    private String gendercd;
    private String nickname;
    private String categcd;
    private String email;
    private String password;
    private String phonenum;
    private String birthymd;
    private String loccd;
    private String seasoncd;
    private String entymd;
    private String quitymd;
    private String imgname;
    private Date credt;
    private Date upddt;
    private String rolecd;
}
