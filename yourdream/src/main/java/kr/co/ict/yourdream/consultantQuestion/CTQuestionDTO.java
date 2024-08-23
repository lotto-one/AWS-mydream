package kr.co.ict.yourdream.consultantQuestion;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor // 필드에 등록된 값을 생성자의 인자로 등록해서 만듬
@NoArgsConstructor // 기본 생성자
@Builder // 빌더패턴 -> 익명 클래스를 사용해서 해당 객체를 단계별로 값을 전달할 때 사용 하는 패턴
// CustomerDTO.builder().name('테스형').age(10).build()
public class CTQuestionDTO {

    private Long cnsqno;
    private Long cnsno;
    private String question;
    private String keyword1;
    private String keyword2;
    private String keyword3;
    private Date credt;
    private Date upddt;
}
