package kr.ict.mydream.vo;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@Alias("cnsquestionvo")
public class CnsQuestionVO {
    private int cnsqno; // 컨설턴트질문번호(PK)
    private int cnsno; // 컨설턴트번호(FK)
    private String question; // 컨설턴트질문
    private String keyword1; // 컨설턴트키워드1
    private String keyword2; // 컨설턴트키워드2
    private String keyword3; // 컨설턴트키워드3
}
