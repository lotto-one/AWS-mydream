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
public class FeedbackDetail_IntResDTO {
    private String cnsfeedbk; // 컨설턴트 피드백(총평)
    private Date upddt;
}
