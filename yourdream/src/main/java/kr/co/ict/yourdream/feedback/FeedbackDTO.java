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
public class FeedbackDTO {

    private Integer memno; // 회원번호(FK)

    private Integer intno; // 면접번호(PK)

    private String name;

    private String inttypecd; // 직무 or 인성면접

    private Date upddt; // 최종수정일자
}
