package kr.co.ict.yourdream.feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumeSelfDTO {
    private int seqno;
    private String title;
    private String content;
}
