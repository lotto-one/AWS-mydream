package kr.co.ict.yourdream.consultantDashBoard;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CnsDashService {

    @Autowired
    private CnsDashRepository cnsDashRepository;

    public int getConsultCount() {
        return cnsDashRepository.countBy();
    }

    public float getMemCnsAverage() {
        return cnsDashRepository.findMemCnsAverage();
    }

    public float getReviewAverage() {
        return cnsDashRepository.findReviewAverage();
    }

    public int getCountConsultingThisWeek() {
        return cnsDashRepository.countCnsScoreThisWeek();
    }

    public List<Map<String, Object>> getCnsStatsByDayOfWeek() {
        return cnsDashRepository.findCnsStatsByDayOfWeek();
    }

    public List<Map<String, Object>> getCategoryCounts() {
        return cnsDashRepository.findCategoryCounts();
    }

    public List<Map<String, Object>> getReviewScoresByRange() {
        return cnsDashRepository.findReviewScoresByRange();
    }

    public List<Map<String, Object>> getAverageScoreByCategoryAndWeekday() {
        return cnsDashRepository.findAverageScoreByCategoryAndWeekday();
    }

}