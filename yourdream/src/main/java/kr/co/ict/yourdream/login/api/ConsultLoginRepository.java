package kr.co.ict.yourdream.login.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ConsultLoginRepository extends JpaRepository<ConsultVO99, Integer>{
    ConsultVO99 findByEmail(String email);

}
