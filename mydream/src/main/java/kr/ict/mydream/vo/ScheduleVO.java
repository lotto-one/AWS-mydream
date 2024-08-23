package kr.ict.mydream.vo;

import java.util.Date;
import org.apache.ibatis.type.Alias;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("schedulevo")
public class ScheduleVO {
    private int schno; // 일정번호(PK)
    private int memno; // 회원번호(FK)
    private String startdt; // 일정시작일시
    private String enddt; // 일정종료일시
    private String title; // 제목
    private String content; // 내용
    private Date credt; // 최초등록일자
    private Date upddt; // 최종수정일자
}