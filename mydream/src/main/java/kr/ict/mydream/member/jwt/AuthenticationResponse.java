package kr.ict.mydream.member.jwt;

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
    private int memno;    
    private String rolecd;
    private int logno; // 추가된 필드
}
