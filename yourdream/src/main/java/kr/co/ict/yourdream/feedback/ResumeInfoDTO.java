package kr.co.ict.yourdream.feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumeInfoDTO {
    private int rsmno;
    private String title;
    private String name;
    private String imgname;
    private String addr;
    private String birthymd;
    private String email;
    private String mphonenum;
    private String hphonenum;
}
