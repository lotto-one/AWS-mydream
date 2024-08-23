package kr.ict.mydream.itvres;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

import kr.ict.mydream.dto.ItvDTO;

@Mapper
public interface ItvDao {

    public List<Map<String, Object>> itvAttitudeList(int memno);

    public List<Map<String, Object>> itvCareerList(int memno);

    public List<ItvDTO> detail(int intno);

    public List<Integer> faceData(int intno);

    public List<Integer> getBarData(int intno);

    public List<Map<String, Object>> getVoiceData(int intno);

    public List<String> wordCloud(int intno);

    public List<Map<String, String>> questionData(int intno);

    public List<String> ctFeedbackList(int intno);

    public void rateUpdate(Map<String, Object> params);
}
