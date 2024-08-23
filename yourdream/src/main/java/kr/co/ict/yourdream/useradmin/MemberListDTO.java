package kr.co.ict.yourdream.useradmin;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberListDTO {
    private Integer memno;
    private String email;
    private String name;
    private String nickname;
    private String gendercd;
    private String birthymd;
    private Date credt;
    private String categcd;
    private String loccd;
    private String phonenum;
    private String consultantName; // 컨설턴트 이름 추가
}
