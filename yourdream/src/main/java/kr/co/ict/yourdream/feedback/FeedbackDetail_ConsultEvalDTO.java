package kr.co.ict.yourdream.feedback;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackDetail_ConsultEvalDTO {
    // private Integer intno; // 면접번호(FK)
    // private Integer qno; // 질문번호(FK)
    // private Integer memno; // 회원번호(FK)
    // private Integer cnsno; // 컨설턴트 번호(FK)
    private String qcnsfeedbk; // 질문에 따른 컨설턴트 피드백
    // private Float qcnsscore; // 질문에 따른 컨설턴트 부여점수
    // private Date credt; // 최초등록일자
    private Date upddt; // 최종수정일자
}
