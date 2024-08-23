package kr.ict.mydream.member.jwt;

import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


// jwt 인증이 실패했을때 처리하는 클래스
@Component
public class JwtAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void afterPropertiesSet() {
        setRealmName("JwT Authentication");
        super.afterPropertiesSet();
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {    
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");    
        response.getWriter().write("{ \"message\": \"" + authException.getMessage() + "\" }");
    }

}
