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
public class FeedbackDetail_InFeedbkDTO {
    private Integer intno;
    private String efeed1;
    private String efeed2;
    private String pfeed1;
    private String pfeed2;
    private String vfeed1;
    private String vfeed2;
    private String sttfeed1;
    private String sttfeed2;
    private Date upddt;
}
