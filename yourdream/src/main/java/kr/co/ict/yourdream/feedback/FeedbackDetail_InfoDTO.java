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
public class FeedbackDetail_InfoDTO {

    private String name;
    private Date upddt;
}
