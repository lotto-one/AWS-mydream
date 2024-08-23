package kr.ict.mydream.vo;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("resumevo")
public class ResumeVO {
    private int rsmno;
    private int memno;
    private String title;
    private String name;
    private String imgname;
    private String addr;
    private String birthymd;
    private String email;
    private String mphonenum;
    private String hphonenum;
    private Date credt;
    private Date upddt;
}
