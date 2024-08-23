package kr.ict.mydream.vo;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@Alias("consultevalvo")
public class ConsultevalVO {

    private int intno; // 면접번호(FK)
    private int qno; // 질문번호(FK)
    private int memno; // 회원번호(FK)
    private int cnsno; // 컨설턴트 번호(FK)
    private String qcnsfeedbk; // 질문에 따른 컨설턴트 피드백
    private float qcnsscore; // 질문에 따른 컨설턴트 부여점수
    private Date credt; // 최초등록일자
    private Date upddt; // 최종수정일자
}
