package kr.co.ict.yourdream.login.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 사용자 인증하기위해 요청하는것
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthenticationRequest { 
    private String email;     
    private String password;  
}
