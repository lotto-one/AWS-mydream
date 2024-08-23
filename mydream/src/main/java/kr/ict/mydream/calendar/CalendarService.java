package kr.ict.mydream.calendar;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.ict.mydream.dto.CalendarDTO;
import kr.ict.mydream.vo.ScheduleVO;

@Service
public class CalendarService {
    @Autowired
    CalendarDao calendarDao;

    public CalendarDTO getCalendar(ScheduleVO vo) {
        CalendarDTO cvo = new CalendarDTO();

        System.out.println("in CalendarService getCalendar vo.getMemno() : "
                + vo.getMemno()
                + vo.getStartdt()
                + vo.getEnddt()); // 이벤트 정보 출력
        // 일정 이벤트 설정
        List<ScheduleVO> svo = calendarDao.list(vo);
        if (svo.size() > 0) {
            List<CalendarDTO.ScheduleEvent> events = new ArrayList<>();

            for (ScheduleVO e : svo) {
                int schno = e.getSchno();
                String title = e.getTitle();
                // Date 형식을 String 형식으로 변환한 값 setting
                String startdt = e.getStartdt();
                String enddt = e.getEnddt();
                events.add(new CalendarDTO.ScheduleEvent(schno, title, startdt, enddt));
            }
            cvo.setEvents(events);

            // D-Day 설정
            List<CalendarDTO.DDay> dDays = new ArrayList<>();
            LocalDate today = LocalDate.now();
            // System.out.println("Today's date: " + today); // 현재 날짜 출력 확인

            for (CalendarDTO.ScheduleEvent event : events) {
                // System.out.println("Processing event: " + event); // 이벤트 정보 출력
                CalendarDTO.DDay dDay = calculateDDay(event.getTitle(), event.getStartdt(),
                        today);
                if (dDay != null) {
                    dDays.add(dDay);
                } else {
                    // System.out.println("No D-Day for event: " + event.getTitle()); // D-Day
                    // 없음
                }
            }
            cvo.setDDays(dDays);
            System.out.println("!! after add dDays  : " + dDays);
        } else {
            // 빈 데이터 객체를 반환하여 클라이언트에서 문제를 감지
            return new CalendarDTO();
        }
        return cvo;
    }

    public CalendarDTO.DDay calculateDDay(String title, String dateStr, LocalDate today) {
        try {
            LocalDate eventDate = LocalDate.parse(dateStr);
            // System.out.println("eventDate:" + eventDate); // 이벤트 날짜 출력 확인
            long daysLeft = ChronoUnit.DAYS.between(today, eventDate);

            if (daysLeft >= 0) {
                // System.out.println("D-Day for event: " + title + " is " + daysLeft + " days
                // away"); // D-Day
                // 출력
                return new CalendarDTO.DDay(title, (int) daysLeft);
            } else {
                // System.out.println("Event already passed: " + title); // 이벤트가 지나간 경우
                return null;
            }
        } catch (DateTimeParseException e) {
            System.err.println("D-Day 계산 중 오류 발생: " + e.getMessage()); // 날짜 파싱 오류
            return null;
        }
    }

    // Scedule TABLE 에 INSERT
    public void addCalendar(ScheduleVO vo) {
        // Scedule TABLE 에 저장
        calendarDao.add(vo);
    }

    // Scedule TABLE 에 UPDATE
    public void updateCalendar(ScheduleVO vo) {
        calendarDao.update(vo);
    }

    // Scedule TABLE 에 DELETE
    public void deleteCalendar(ScheduleVO vo) {
        calendarDao.delete(vo);
    }

}