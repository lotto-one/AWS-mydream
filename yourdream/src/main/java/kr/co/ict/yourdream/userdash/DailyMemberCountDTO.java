package kr.co.ict.yourdream.userdash;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DailyMemberCountDTO {
    private Date date;
    private long count;
}
