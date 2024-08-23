package kr.co.ict.yourdream.login.api;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CnsCareerRepositoryBok extends JpaRepository<CnsCareerVO99, CnsCareerId> {
    Optional<Integer> findTopByCnsnoOrderBySeqnoDesc(int cnsno);
}