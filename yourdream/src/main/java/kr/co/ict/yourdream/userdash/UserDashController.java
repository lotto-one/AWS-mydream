package kr.co.ict.yourdream.userdash;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/members")
public class UserDashController {

    @Autowired
    private UserDashService userDashService;

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalMembers() {
        long totalMembers = userDashService.getTotalMembers();
        return ResponseEntity.ok(totalMembers);
    }
    
    @GetMapping("/daily-visit-count")
    public long getDailyVisitCount() {
        return userDashService.getDailyVisitors();
    }

    @GetMapping("/int-count")
    public long getTotalIntnos() {
        return userDashService.getTotalIntnos();
    }

    @GetMapping("/con-count")
    public long getTotalCount() {
        return userDashService.getTotalCount();
    }

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat DISPLAY_FORMAT = new SimpleDateFormat("M월 d일");

    @GetMapping("/dailyCounts")
    public Map<String, Long> getDailyMemberCounts(
            @RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr) {

        Date startDate;
        Date endDate;
        try {
            startDate = DATE_FORMAT.parse(startDateStr);
            endDate = DATE_FORMAT.parse(endDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return Collections.emptyMap(); // Return an empty map if date parsing fails
        }

        // Get the list of counts from the service
        List<DailyMemberCountDTO> counts = userDashService.getDailyMemberCounts(startDate, endDate);

        // Convert the list to a map with dates as keys
        Map<String, Long> countMap = counts.stream()
                .collect(Collectors.toMap(
                        count -> DISPLAY_FORMAT.format(count.getDate()), // Format the date as "M월 d일"
                        DailyMemberCountDTO::getCount, // Map the count
                        Long::sum // Merge function to sum up counts for the same date
                ));

        // Generate all dates between startDate and endDate
        List<String> allDates = generateDateRange(startDate, endDate);

        // Ensure all dates are included in the result map
        Map<String, Long> resultMap = new LinkedHashMap<>();
        for (String dateStr : allDates) {
            resultMap.put(dateStr, countMap.getOrDefault(dateStr, 0L));
        }

        return resultMap;
    }

    // Helper method to generate a list of dates between startDate and endDate
    private List<String> generateDateRange(Date startDate, Date endDate) {
        List<String> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        
        while (!calendar.getTime().after(endDate)) {
            dates.add(DISPLAY_FORMAT.format(calendar.getTime())); // Use display format here
            calendar.add(Calendar.DATE, 1);
        }
        
        return dates;
    }


    @GetMapping("/categcdCounts")
    public Map<String, Integer> getCategcdCounts() {
        List<Map<String, Object>> rawData = userDashService.getMemberCountByCategcd();
        Map<String, Integer> formattedData = new HashMap<>();

        for (Map<String, Object> entry : rawData) {
            String categCd = (String) entry.get("categCd");
            Integer count = ((Number) entry.get("count")).intValue();
            formattedData.put(categCd, count);
        }

        return formattedData;
    }
    

    @GetMapping("/loccdCounts")
    public Map<String, Integer> getLoccdCounts() {
        return userDashService.getLoccdCounts();
    }


    @GetMapping("/daily-interview")
    public Map<String, Long> getDailyIntTypeCounts(
            @RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr) throws ParseException {

        Date startDate = DATE_FORMAT.parse(startDateStr);
        Date endDate = DATE_FORMAT.parse(endDateStr);

        return userDashService.getDailyIntTypeCounts(startDate, endDate);
    }
}
