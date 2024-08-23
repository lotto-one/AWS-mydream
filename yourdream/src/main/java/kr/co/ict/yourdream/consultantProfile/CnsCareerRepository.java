package kr.co.ict.yourdream.consultantProfile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CnsCareerRepository extends JpaRepository<CnsCareer, CnsCareerPK> {
    List<CnsCareer> findByConsultProfile(ConsultProfile consultProfile);
}