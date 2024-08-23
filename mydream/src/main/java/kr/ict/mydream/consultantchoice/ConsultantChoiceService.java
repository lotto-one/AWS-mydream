package kr.ict.mydream.consultantchoice;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.ict.mydream.vo.ConsultVO;

@Service
public class ConsultantChoiceService {

    @Autowired
    private ConsultantChoiceDao consultantChoiceDao;

    // 컨설턴트 리스트
    public List<ConsultVO> consultantList() {
        return consultantChoiceDao.consultantList();
    }

    // 컨설턴트 유무
    public Map<String, Object> consultantUse(int memno) {
        return consultantChoiceDao.consultantUse(memno);
    };

    // 컨설턴트 신청 로그 유무
    public int consultantHistyory(Map<String, Integer> data) {
        return consultantChoiceDao.consultantHistyory(data);
    }

    // 컨설턴트 신청 (insert)
    public void consultantAppl(Map<String, Integer> data) {
        consultantChoiceDao.consultantAppl(data);
    }

    // 컨설턴트 신청 (update)
    public void consultantApplUp(Map<String, Integer> data) {
        consultantChoiceDao.consultantApplUp(data);
    }

    // 컨설턴트 취소
    public void consultantCancle(int memno) {
        consultantChoiceDao.consultantCancle(memno);
    }

    // 컨설턴트 챗룸 생성
    public void consultantChatCreate(Map<String, Integer> data) {
        consultantChoiceDao.consultantChatCreate(data);
    }
}
