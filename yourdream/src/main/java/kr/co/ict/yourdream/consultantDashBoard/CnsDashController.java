package kr.co.ict.yourdream.consultantDashBoard;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/admin/cnsDash")
public class CnsDashController {

    @Autowired
    private CnsDashService cnsDashService;

    @GetMapping("/getcnscount")
    public int getConsultCount() {
        return cnsDashService.getConsultCount();
    }

    @GetMapping("/memcns_ave")
    public float getMemCnsAverage() {
        return cnsDashService.getMemCnsAverage();
    }

    @GetMapping("/review_ave")
    public float getReviewAverage() {
        return cnsDashService.getReviewAverage();
    }

    @GetMapping("/cntconsulting_thisweek")
    public int getCountConsultingThisWeek() {
        return cnsDashService.getCountConsultingThisWeek();
    }

    @GetMapping("/cntconsulting_day")
    public List<Map<String, Object>> getCnsStatsByDayOfWeek() {
        return cnsDashService.getCnsStatsByDayOfWeek();
    }

    @GetMapping("/getcategcnt")
    public List<Map<String, Object>> getCategoryCounts() {
        return cnsDashService.getCategoryCounts();
    }

    @GetMapping("/reviewrange")
    public List<Map<String, Object>> getReviewScoresByRange() {
        return cnsDashService.getReviewScoresByRange();
    }

    @GetMapping("/daycategave")
    public List<Map<String, Object>> getAverageScoreByCategoryAndWeekday() {
        return cnsDashService.getAverageScoreByCategoryAndWeekday();
    }
}
