package kr.co.ict.yourdream.consultantProfile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface ConsultProfileRepository extends JpaRepository<ConsultProfile, Integer> {

    // ID로 컨설턴트 프로필 조회, 경력 정보 포함
    @Query("SELECT cp FROM ConsultProfile cp LEFT JOIN FETCH cp.cnscareerList WHERE cp.cnsno = :cnsno")
    Optional<ConsultProfile> findByIdWithCareers(@Param("cnsno") int cnsno);

    // 프로필 이미지 정보 업데이트
    @Modifying
    @Query(value = "UPDATE TBCONSULT SET IMGNAME = :imgname WHERE CNSNO = :cnsno", nativeQuery = true)
    int updateProfileImage(@Param("cnsno") int cnsno, @Param("imgname") String imgname);

    // ID로 컨설턴트 프로필 조회 (기본 정보만)
    @Query("SELECT cp FROM ConsultProfile cp WHERE cp.cnsno = :cnsno")
    Optional<ConsultProfile> findBasicProfileById(@Param("cnsno") int cnsno);
}