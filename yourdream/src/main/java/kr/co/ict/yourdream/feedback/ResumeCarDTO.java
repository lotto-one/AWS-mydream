package kr.co.ict.yourdream.feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumeCarDTO {
    private int seqno;
    private String entymd;
    private String quitymd;
    private String compname;
    private String jobclass;
    private String maintask;
    private String jobposition;

}
