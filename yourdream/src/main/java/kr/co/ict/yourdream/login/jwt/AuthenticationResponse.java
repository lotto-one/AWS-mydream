package kr.co.ict.yourdream.login.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 사용자 인증 후 반환되는 응답 데이터
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse { 
    private String accessToken;
    private int cnsno;    
    private String rolecd;
}
