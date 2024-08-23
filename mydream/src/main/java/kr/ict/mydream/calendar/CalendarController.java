package kr.ict.mydream.calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.ict.mydream.dto.CalendarDTO;
import kr.ict.mydream.vo.ScheduleVO;

@RestController
@RequestMapping("/schedule")
public class CalendarController {
    @Autowired
    private CalendarService calendarService;

    @GetMapping
    public ResponseEntity<CalendarDTO> getCalendar(ScheduleVO vo) {
        // System.out.println("===== in CalendarController getCalendar");
        CalendarDTO cvo = calendarService.getCalendar(vo);
        if (cvo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // System.out.println("CalendarVO events: " + vo.getEvents());
        // System.out.println("CalendarVO dDays: " + vo.getDDays());
        return new ResponseEntity<>(cvo, HttpStatus.OK);
    }

    // Scedule 저장
    @PostMapping("/add")
    public ResponseEntity<ScheduleVO> addCalendar(@RequestBody ScheduleVO vo) {
        // System.out.println("in addCalendar ::: SceduleVO: " + vo.getMemno() + ", " +
        // vo.getTitle() + ", "
        // + vo.getStartdt() + ", " + vo.getEnddt());
        calendarService.addCalendar(vo);
        // System.out.println("Saved SceduleVO: " + vo);
        return ResponseEntity.ok().body(vo);
    }

    // Scedule 수정
    @PostMapping("/update")
    public ResponseEntity<ScheduleVO> updateCalendar(@RequestBody ScheduleVO vo) {
        // System.out.println("Updating SceduleVO: " + vo.getSchno() + ", " +
        // vo.getMemno() + ", " + vo.getTitle() + ", "
        // + vo.getStartdt() + ", " + vo.getEnddt());
        calendarService.updateCalendar(vo);
        return ResponseEntity.ok().body(vo);
    }

    // Scedule 삭제
    @PostMapping("/delete")
    public ResponseEntity<Void> deleteCalendar(@RequestBody ScheduleVO vo) {
        // System.out.println("Deleting SceduleVO with schno: " + vo.getSchno());
        calendarService.deleteCalendar(vo);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}