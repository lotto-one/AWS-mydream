package kr.co.ict.yourdream.useradmin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateDTO {
    private Integer memno; // 타입 수정
    private String email;
    private String name;
    private String nickname;
    private String gendercd;
    private String categcd;
    private String loccd;
    private String phonenum;
}
