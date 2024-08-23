package kr.co.ict.yourdream.consultantQuestion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CTQuestionService {

    @Autowired
    private CTQuestionRepository ctQuestionRepository;

    public CTQuestion create(CTQuestionDTO dto) {
        CTQuestion entity = CTQuestion.builder()
                .cnsqno(dto.getCnsqno())
                .cnsno(dto.getCnsno())
                .question(dto.getQuestion())
                .keyword1(dto.getKeyword1())
                .keyword2(dto.getKeyword2())
                .keyword3(dto.getKeyword3())
                .credt(new Date())
                .upddt(new Date())
                .build();
        return ctQuestionRepository.save(entity);
    }

    // 수정
    public CTQuestion update(CTQuestionDTO dto) {
        // entity안에 PK 존재 유무에 따라 입력, 수정 -save(entity)
        CTQuestion entity = ctQuestionRepository.findById(dto.getCnsqno())
                .orElseThrow(() -> new RuntimeException("수정할 수 없다."));
        entity.setQuestion(dto.getQuestion());
        entity.setKeyword1(dto.getKeyword1());
        entity.setKeyword2(dto.getKeyword2());
        entity.setKeyword3(dto.getKeyword3());
        entity.setUpddt(new Date());
        return ctQuestionRepository.save(entity);
    }

    public List<CTQuestionDTO> list(Long cnsno) {
        List<CTQuestion> entityList = ctQuestionRepository.findByCnsnoOrderByCnsqnoAsc(cnsno);
        List<CTQuestionDTO> list = new ArrayList<>();

        for (CTQuestion entity : entityList) {
            CTQuestionDTO vo = CTQuestionDTO.builder()
                    .cnsqno(entity.getCnsqno())
                    .cnsno(entity.getCnsno())
                    .question(entity.getQuestion())
                    .keyword1(entity.getKeyword1())
                    .keyword2(entity.getKeyword2())
                    .keyword3(entity.getKeyword3())
                    .credt(entity.getCredt())
                    .upddt(entity.getUpddt())
                    .build();
            list.add(vo);
        }
        return list;
    }

    public void delete(Long cnsqno) {
        CTQuestion entity = ctQuestionRepository.findById(cnsqno)
                .orElseThrow(() -> new RuntimeException("삭제할 수 없습니다."));
        ctQuestionRepository.delete(entity);
    }
}
