package kr.ict.mydream.consultantchoice;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.ict.mydream.vo.ConsultVO;

@Mapper
public interface ConsultantChoiceDao {

    public List<ConsultVO> consultantList(); // 컨설턴트 리스트

    public Map<String, Object> consultantUse(int memno); // 컨설턴트 유무

    public int consultantHistyory(Map<String, Integer> data); // 컨설턴트 신청 로그 유무

    public void consultantAppl(Map<String, Integer> data); // 컨설턴트 신청 (insert)

    public void consultantApplUp(Map<String, Integer> data); // 컨설턴트 신청 (update)

    public void consultantCancle(int memno); // 컨설턴트 취소

    public void consultantChatCreate(Map<String, Integer> data); // 컨설턴트 챗룸 생성

}
