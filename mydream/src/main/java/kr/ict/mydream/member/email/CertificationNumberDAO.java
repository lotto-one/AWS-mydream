package kr.ict.mydream.member.email;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Repository
public class CertificationNumberDAO {
    
    private final StringRedisTemplate stringRedisTemplate;

    public void saveCertificationNumber(String email, String authCode) {
        stringRedisTemplate.opsForValue().set(email, authCode, Duration.ofSeconds(10000));
    }
     // 이메일 인증번호 가져오기
     public String getCertificationNumber(String email) {
        return stringRedisTemplate.opsForValue().get(email);
    }

    // 이메일 인증번호 삭제
    public void deleteCertificationNumber(String email) {
        stringRedisTemplate.delete(email);
    }

    // 이메일 인증번호 존재 여부
    public boolean hasKey(String email) {
        return stringRedisTemplate.hasKey(email);
    }
}
