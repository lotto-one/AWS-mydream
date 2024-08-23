package kr.co.ict.yourdream.consultantQuestion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CTQuestionRepository extends JpaRepository<CTQuestion, Long> {

    List<CTQuestion> findByCnsnoOrderByCnsqnoAsc(Long cnsno);

}
