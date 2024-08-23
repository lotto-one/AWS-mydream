package kr.co.ict.yourdream.login.auth;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChangePasswordRequest {
    @NotNull
    private Integer cnsno;
    @NotNull
    private String currentPassword;
    @NotNull
    private String newPassword;
    
}
