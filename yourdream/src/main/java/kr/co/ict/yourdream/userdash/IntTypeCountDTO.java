package kr.co.ict.yourdream.userdash;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class IntTypeCountDTO {
    private Date credt; // 날짜
    private long count; // 카운트
}