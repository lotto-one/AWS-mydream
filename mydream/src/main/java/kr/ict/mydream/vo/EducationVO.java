package kr.ict.mydream.vo;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("educationvo")
public class EducationVO {
    private int seqno;
    private int rsmno;
    private String entymd;
    private String gradeymd;
    private String schoolname;
    private String major;
    private String gradeuateyn;
    private String gradescore;
    private Date credt;
    private Date upddt;
}
