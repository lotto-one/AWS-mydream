package kr.ict.mydream.member.auth;

import java.util.Date;
import java.util.UUID;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import kr.ict.mydream.member.jwt.AuthenticationRequest;
import kr.ict.mydream.member.jwt.AuthenticationResponse;
import kr.ict.mydream.member.jwt.JwtTokenProvider;
import kr.ict.mydream.member.login.MemberLoginService;
import kr.ict.mydream.vo.LoginVO;
import kr.ict.mydream.vo.MemberVO;




@RestController
@CrossOrigin 
@RequestMapping("/membership")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private MemberLoginService memberLoginService;
    


    // private final String filePath;

    private final Path uploadDir2 = Paths.get("uploads");
    private final String uploadDir =  Paths.get("").toAbsolutePath().toString() + "/uploads";

@PostMapping("/login")
public ResponseEntity<?> authenticateUser(@RequestBody AuthenticationRequest authenticationRequest) {
    try {
        // 이메일과 비밀번호로 인증 시도
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), 
                                                    authenticationRequest.getPassword()));

        // 인증 성공 시 JWT 토큰 생성
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.createToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 사용자 정보 가져오기
        MemberVO member = memberLoginService.findByEmail(userDetails.getUsername());
        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        // 사용자 정보 출력 (로그 확인용)
        System.out.println(member.getEmail());
        System.out.println(member.getPassword());


        int memno = member.getMemno(); 
        String rolecd = member.getRolecd(); 

         // 로그인 기록 삽입
        LoginVO loginVO = new LoginVO();
        loginVO.setMemno(member.getMemno());
        loginVO.setLogindt(new Date()); // 로그인 시간을 현재 시간으로 설정
        memberLoginService.insertLoginRecord(loginVO); // 로그인 기록 삽입

         // 로그 출력
         

         // 로그인 기록에서 생성된 logno 가져오기
         int logno = loginVO.getLogno();
         System.out.println("Inserted login record with logno: " + loginVO.getLogno());
        // 성공 시 JWT 토큰과 사용자 정보 반환
        return ResponseEntity.ok(new AuthenticationResponse(jwt, memno, rolecd,logno));

    } catch (BadCredentialsException ex) {
        // 인증 실패 시 비밀번호가 잘못되었다는 메시지 반환
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 잘못되었습니다.");
    } catch (Exception ex) {
        // 기타 오류 시 일반적인 오류 메시지 반환
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그인 중 오류가 발생했습니다.");
    }
}
    
    
    @GetMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestParam(name = "logno") int logno) {
        try {
            // 로그아웃 기록 업데이트
            memberLoginService.updateLogoutRecord(logno);

            // 로그아웃 처리
            SecurityContextHolder.clearContext();
            return ResponseEntity.ok("Logout successful");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그아웃 중 오류가 발생했습니다.");
        }
    }


    // http://192.168.0.73/mydream/membership/register 
   @PostMapping("/register")
   public ResponseEntity<?> registerUser(@RequestBody MemberVO member) {
       if (memberLoginService.findByEmail(member.getEmail()) != null) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용중인 이메일");
       }

       member.setPassword(memberLoginService.encodePassword(member.getPassword()));
       try {
           memberLoginService.registerMember(member);
           return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
       } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while registering the user");
       }
   }

   // http://192.168.0.54/mydream/mypage/profile?memno=51 성공
   @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestParam("memno") int memno, @RequestBody MemberVO member) {
        System.out.println("memno값 :"+ memno);
        System.out.println("vo전달값"+member.getPassword());
        memberLoginService.updateProfile(memno,member);
        return ResponseEntity.ok("Profile updated successfully");
    }


    // http://192.168.0.54/mydream/mypage/profile?memno=51 성공
    @GetMapping("/profile")
    public MemberVO myprofile(@RequestParam("memno") int memno) {
        return memberLoginService.mypage(memno);
    }

    @PostMapping("/profileImage")
    public ResponseEntity<?> updateImage(@RequestParam("file") MultipartFile mf, @RequestParam("memno") int memno) {
        if (mf == null || mf.isEmpty()) {
            return ResponseEntity.badRequest().body("파일이 전송되지 않았습니다.");
        }

        String oriFn = mf.getOriginalFilename();
    String fileExtension = ""; // 파일 확장자 저장

    if (oriFn != null && oriFn.contains(".")) {
        fileExtension = oriFn.substring(oriFn.lastIndexOf(".")); // 확장자 추출
    }

    // UUID 생성 및 파일 이름 설정
    String newFileName = UUID.randomUUID().toString() + fileExtension;
    Path destinationPath = Paths.get(uploadDir).resolve(newFileName).normalize(); // 경로를 안전하게 조작
    System.out.println("uploadir"+uploadDir);
    System.out.println("uploadir2"+uploadDir2);
    System.out.println("FullPath :" + destinationPath);

    try {
        // 파일 복사 및 덮어쓰기
        Files.copy(mf.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

        MemberVO vo = new MemberVO();
        vo.setMemno(memno);
        vo.setImgname(newFileName); // UUID 파일 이름을 데이터베이스에 저장
        memberLoginService.updateImage(vo);

        return ResponseEntity.ok().body(newFileName); // UUID 파일 이름 반환
    } catch (IOException e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body("이미지 업로드 중 오류가 발생했습니다.");
    }
    }


    



    // 비밀번호 변경
    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        if (changePasswordRequest.getCurrentPassword() == null || changePasswordRequest.getNewPassword() == null) {
            return ResponseEntity.badRequest().body("Current or new password cannot be null");
        }

        boolean success = memberLoginService.changePassword(
            changePasswordRequest.getMemno(), 
            changePasswordRequest
        );
        if (success) {
            return ResponseEntity.ok("Password changed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password change failed");
        }
    }

    @DeleteMapping("/deleteMember")
    public ResponseEntity<String> deleteMember(@RequestParam("memno") int memno) {
        System.out.println("탈퇴 회원넘버 도착"+memno);
        memberLoginService.deletemember(memno);
        return ResponseEntity.ok("성공적으로 멤버 삭제완료");
    }
}