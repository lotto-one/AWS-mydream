package kr.co.ict.yourdream.userdash;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDashService {

    @Autowired
    private UserDashRepository userDashRepository;


    public long getTotalMembers(){
        return userDashRepository.countTotalMembers();
    }
    
    public long getDailyVisitors() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date today = calendar.getTime();
        return userDashRepository.countDailyVisitors(today);
    }

    public long getTotalIntnos() {
        return userDashRepository.countTotalIntnos();
    }

    public long getTotalCount() {
        return userDashRepository.countTotal();
    }
    

    public List<DailyMemberCountDTO> getDailyMemberCounts(Date startDate, Date endDate) {
    // Set endDate to the end of the day
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(endDate);
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    calendar.set(Calendar.MILLISECOND, 999);
    Date endOfDay = calendar.getTime();

    return userDashRepository.findDailyCounts(startDate, endOfDay);
}


public List<Map<String, Object>> getMemberCountByCategcd() {
    return userDashRepository.findMemberCountByCategcd();
}


public Map<String, Integer> getLoccdCounts() {
    List<Map<String, Object>> loccdCounts = userDashRepository.findMemberCountByLoccd();
    Map<String, Integer> formattedLoccdCounts = new HashMap<>();

    for (Map<String, Object> entry : loccdCounts) {
        String locCd = entry.get("locCd").toString(); // locCd를 문자열로 변환
        Integer count = ((Number) entry.get("count")).intValue(); // count를 정수로 변환
        formattedLoccdCounts.put(locCd, count);
    }

    return formattedLoccdCounts;
}



private static final SimpleDateFormat DISPLAY_FORMAT = new SimpleDateFormat("M월 d일");

    public Map<String, Long> getDailyIntTypeCounts(Date startDate, Date endDate) {
        // Set endDate to the end of the day
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(endDate);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date endOfDay = calendar.getTime();

        // Get the list of counts from the repository
        List<IntTypeCountDTO> counts = userDashRepository.findDailyIntTypeCounts(startDate, endOfDay);

        // Convert the list to a map with dates as keys
        Map<String, Long> countMap = counts.stream()
                .collect(Collectors.toMap(
                        count -> DISPLAY_FORMAT.format(count.getCredt()), // Format the date as "M월 d일"
                        IntTypeCountDTO::getCount, // Map the count
                        Long::sum // Merge function to sum up counts for the same date
                ));

        // Generate all dates between startDate and endDate
        List<String> allDates = generateDateRange(startDate, endOfDay);

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

}
