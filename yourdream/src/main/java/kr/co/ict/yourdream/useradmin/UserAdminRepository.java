package kr.co.ict.yourdream.useradmin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface UserAdminRepository extends JpaRepository<MemberVOBok,Integer> {
        



    @Query("SELECT m FROM MemberVOBok m")
    List<MemberVOBok> findAllMembers();

    @Query("SELECT new kr.co.ict.yourdream.useradmin.MemberListDTO(m.memno, m.email, m.name, m.nickname, m.gendercd, m.birthymd, m.credt, m.categcd, m.loccd, m.phonenum, COALESCE(c.name, '')) " +
           "FROM MemberVOBok m " +
           "LEFT JOIN MemberConsultVOBok mc ON m.memno = mc.memno " +
           "LEFT JOIN ConsultVOBok c ON mc.cnsno = c.cnsno " +
           "WHERE mc.useyn = 'Y' OR mc.useyn IS NULL " +
           "ORDER BY m.memno")
        List<MemberListDTO> findAllMemberDetailsWithConsultName();


    @Modifying
    @Query("UPDATE MemberVOBok m SET m.email = :email, m.name = :name, m.nickname = :nickname, " +
        "m.gendercd = :gendercd, m.categcd = :categcd, m.loccd = :loccd, m.phonenum = :phonenum " +
        "WHERE m.memno = :memno")
    void updateMember(
        @Param("memno") Integer memno,  // Integer로 변경
        @Param("email") String email,
        @Param("name") String name,
        @Param("nickname") String nickname,
        @Param("gendercd") String gendercd,
        @Param("categcd") String categcd,
        @Param("loccd") String loccd,
        @Param("phonenum") String phonenum);


    
}
