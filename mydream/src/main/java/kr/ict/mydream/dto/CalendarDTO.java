package kr.ict.mydream.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CalendarDTO {

    // 일정관리
    private List<ScheduleEvent> events;
    private List<DDay> dDays;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ScheduleEvent {
        private int schno;
        private String title;
        private String startdt;
        private String enddt;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DDay {
        private String title;
        private int daysLeft;
    }

}
