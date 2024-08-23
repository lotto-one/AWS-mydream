package kr.ict.mydream.member.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kr.ict.mydream.member.auth.ChangePasswordRequest;
import kr.ict.mydream.vo.LoginVO;
import kr.ict.mydream.vo.MemberVO;


@Service
public class MemberLoginService {
    @Autowired
    private MemberLoginDAO memberLoginDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public void registerMember(MemberVO member) {
        memberLoginDAO.registerMember(member);
    }

    public MemberVO findByEmail(String email) {
        return memberLoginDAO.findByEmail(email);
    }

    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    // http://192.168.0.73/mydream/membership/profile?memno=51
    public void updateProfile(int memno, MemberVO member) {
        System.out.println("서비스에서 vo확인"+member.getPassword());
        if (member.getPassword() != null && !member.getPassword().isEmpty()) {
            member.setPassword(passwordEncoder.encode(member.getPassword()));
        }
        System.out.println("인코딩후 확인"+member.getPassword());

        member.setMemno(memno);

        memberLoginDAO.updateProfile(member);
    }






    public MemberVO mypage(int memno){
        return memberLoginDAO.mypage(memno);
    }


    public void updateImage(MemberVO member) {
        memberLoginDAO.updateImage(member);
    }


    public boolean changePassword(int memno, ChangePasswordRequest changePasswordRequest) {
        try {
            String currentPasswordFromDB = memberLoginDAO.getPasswordByMemno(memno);
            
            // 로그 추가
            System.out.println("Current password from DB: " + currentPasswordFromDB);
            System.out.println("Current password from request: " + changePasswordRequest.getCurrentPassword());
    
            if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), currentPasswordFromDB)) {
                throw new IllegalArgumentException("현재 비밀번호가 올바르지 않습니다.");
            }
    
            String encodedNewPassword = passwordEncoder.encode(changePasswordRequest.getNewPassword());
            int rowsAffected = memberLoginDAO.updatePassword(memno, encodedNewPassword);
    
            // 로그 추가
            System.out.println("Rows affected by update: " + rowsAffected);
    
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("비밀번호 변경 중 오류 발생", e);
        }
    }
    

    // 로그인 기록 삽입
    public void insertLoginRecord(LoginVO login) {
       memberLoginDAO.insertLoginRecord(login);
       int logno = memberLoginDAO.selectLastInsertId();
       login.setLogno(logno);
    }

    // 로그아웃 기록 업데이트
    public void updateLogoutRecord(int logno) {
        memberLoginDAO.updateLogoutRecord(logno);
    }
    

    public void deletemember(int memno){
        System.out.println("탈퇴서비스도착"+memno);
        memberLoginDAO.deletemember(memno);
    }

    
}
