package kr.ict.mydream.member.login;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.ict.mydream.vo.LoginVO;
import kr.ict.mydream.vo.MemberVO;



@Mapper
public interface MemberLoginDAO {
    MemberVO findByEmail(String email);
    int registerMember(MemberVO member);
    void updateProfile(MemberVO member);


    public MemberVO mypage(int memno);    
    public void updateImage(MemberVO member);

    String getPasswordByMemno(int memno);
    int updatePassword(@Param("memno") int memno, @Param("newPassword") String newPassword);

    // 로그인 기록 삽입
    void insertLoginRecord(LoginVO login);
    int selectLastInsertId();
    // 로그아웃 기록 업데이트
    void updateLogoutRecord(int logno);

    void deletemember(int memno);
} 
