package kr.co.ict.yourdream.login.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import kr.co.ict.yourdream.login.api.ConsultDTO;
import kr.co.ict.yourdream.login.api.ConsultLoginService;
import kr.co.ict.yourdream.login.api.ConsultVO99;
import kr.co.ict.yourdream.login.jwt.AuthenticationRequest;
import kr.co.ict.yourdream.login.jwt.AuthenticationResponse;
import kr.co.ict.yourdream.login.jwt.JwtTokenProvider;




@RestController
@CrossOrigin
@RequestMapping("/admin/membership")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    @Autowired
    private ConsultLoginService consultLoginService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.createToken(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            ConsultVO99 consul = consultLoginService.findByEmail(userDetails.getUsername());
            if (consul == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
            }
            int cnsno = consul.getCnsno();
            String rolecd = consul.getRolecd();
            return ResponseEntity.ok(new AuthenticationResponse(jwt, cnsno, rolecd));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout successful");
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody ConsultVO99 consul) {
        if (consultLoginService.findByEmail(consul.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already in use");
        }
        consul.setPassword(consultLoginService.encodePassword(consul.getPassword()));
        try {
            consultLoginService.registerConsultant(consul);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while registering the user");
        }
    }

    @PutMapping("/updatePassword")
    public void updatePassword(
            @RequestParam(name = "cnsno") Integer cnsno, 
            @RequestParam(name = "newPassword") String newPassword) {
        consultLoginService.updatePassword(cnsno, newPassword);
    }



    @PostMapping("/register2")
public ResponseEntity<Map<String, String>> registerConsultant2(@RequestBody ConsultDTO consultDTO) {
    // 서비스 호출하여 컨설턴트와 경력 정보를 등록
    consultLoginService.registerConsultant2(consultDTO);

    Map<String, String> response = new HashMap<>();
    response.put("message", "컨설턴트 및 경력 정보가 성공적으로 등록되었습니다.");
    return ResponseEntity.ok(response);
}

     
}
