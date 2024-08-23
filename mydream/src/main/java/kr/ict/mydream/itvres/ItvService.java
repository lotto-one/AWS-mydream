package kr.ict.mydream.itvres;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.ict.mydream.dto.ItvDTO;

import java.util.Map;

@Service
public class ItvService {
    @Autowired
    private ItvDao itvDao;

    public List<Map<String, Object>> itvAttitudeList(int memno) {
        return itvDao.itvAttitudeList(memno);
    }

    public List<Map<String, Object>> itvCareerList(int memno) {
        return itvDao.itvCareerList(memno);
    }

    public List<ItvDTO> detail(int intno) {
        return itvDao.detail(intno);
    }

    public List<Integer> faceData(int intno) {
        return itvDao.faceData(intno);
    }

    public List<Integer> getBarData(int intno) {
        return itvDao.getBarData(intno);
    }

    public List<String> wordCloud(int intno) {
        return itvDao.wordCloud(intno);
    }

    public List<Map<String, Object>> getVoiceData(int intno) {
        return itvDao.getVoiceData(intno);
    }

    public List<Map<String, String>> questionData(int intno) {
        return itvDao.questionData(intno);
    }

    public List<String> ctFeedbackList(int intno) {
        return itvDao.ctFeedbackList(intno);
    }

    public void updateReviewScore(int intno, double reviewscore) {
        Map<String, Object> params = new HashMap<>();
        params.put("intno", intno);
        params.put("reviewscore", reviewscore);
        System.out.println("intno" + intno + "reviewscore" + reviewscore);
        itvDao.rateUpdate(params);
    }
}