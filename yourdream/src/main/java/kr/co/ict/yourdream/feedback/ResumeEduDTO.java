package kr.co.ict.yourdream.feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumeEduDTO {
    private int seqno;
    private String entymd;
    private String gradeymd;
    private String schoolname;
    private String major;
    private String gradeuateyn;
    private String gradescore;
}
