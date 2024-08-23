package kr.ict.mydream.member.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChangePasswordRequest {
    private int memno;
    private String currentPassword;
    private String newPassword;
    
}
