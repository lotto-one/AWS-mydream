package kr.ict.mydream.vo;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@Alias("memberconsultvo")
public class MemberConsultVO {

    private int cnsno; // 회원번호(FK)
    private int memno; // 컨설턴트번호(FK)
    private String useyn; // 사용여부
    private Date credt; // 최초등록일자
    private Date upddt; // 최초수정일자

    private ConsultVO consultant; // 매핑된 컨설턴트 정보
}
