package kr.ict.mydream.calendar;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.ict.mydream.vo.ScheduleVO;

@Mapper
public interface CalendarDao {
    public List<ScheduleVO> list(ScheduleVO vo);

    public void add(ScheduleVO vo);

    public void update(ScheduleVO vo);

    public void delete(ScheduleVO vo);
}