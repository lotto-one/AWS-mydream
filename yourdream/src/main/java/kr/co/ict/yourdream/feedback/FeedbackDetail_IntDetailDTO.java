package kr.co.ict.yourdream.feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackDetail_IntDetailDTO {
    // private Integer qno; // 질문번호(PK)
    // private Integer aiqno; // AI질문번호
    // private Integer cnsqno; // 컨설턴트질문번호
    private String question; // 질문제목
    private String answer; // 질문답변 STT
    // private Float ecntgood; // 감정긍정count
    // private Float ecntsoso; // 감정중립count
    // private Float ecntbad; // 감정부정count
    // private Float pbadcnt; // 자세불량count
    private Float vhertz; // 음성주파수
    private Float vjitter; // 음성진폭
    private Float vspeed; // 음성공백시간
    private String aifeedbk; // AI피드백디테일
    private Float escore; // 감정평균점수 (이걸 기준으로 상중하)
    private Float pscore; // 자세평균점수 (이걸 기준으로 상중하)
    // private Float vscore; // 음성평균점수 (이걸 기준으로 상중하)
    // private Date upddt;
}
