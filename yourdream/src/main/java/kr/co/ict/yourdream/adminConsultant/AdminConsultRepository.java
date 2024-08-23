package kr.co.ict.yourdream.adminConsultant;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import kr.co.ict.yourdream.consultantProfile.ConsultProfile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminConsultRepository extends JpaRepository<ConsultProfile, Integer> {

    // 모든 컨설턴트 프로필을 경력 정보와 함께 조회
    @Query("SELECT DISTINCT cp FROM ConsultProfile cp LEFT JOIN FETCH cp.cnscareerList WHERE cp.rolecd = 'C' ORDER BY cp.cnsno ASC")
    List<ConsultProfile> findAllWithCareers();

    // 특정 ID의 컨설턴트 프로필을 경력 정보와 함께 조회
    @Query("SELECT cp FROM ConsultProfile cp LEFT JOIN FETCH cp.cnscareerList WHERE cp.cnsno = :cnsno")
    Optional<ConsultProfile> findByIdWithCareers(@Param("cnsno") int cnsno);
}