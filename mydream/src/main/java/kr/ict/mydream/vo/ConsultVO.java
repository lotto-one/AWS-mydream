package kr.ict.mydream.vo;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@Alias("consultvo")
public class ConsultVO {
    private int cnsno; // 컨설턴트 번호(PK)
    private String name; // 컨설턴트 성명
    private String gendercd; // 컨설턴트 성별
    private String categcd; // 전문분야
    private String email; // 컨설턴트이메일
    private String phonenum; // 컨설턴트 휴대폰번호
    private String birthymd; // 컨설턴트 생년월일
    private String introduce; // 컨설턴트 자기소개
    private String quitymd; // 컨설턴트 퇴사일자
    private String imgname; // 사진이름
    private String cnscareer; // 컨설팅경력
    private String cnsproject; // 프로젝트수행
    private Date credt; // 최초등록일자
    private Date upddt; // 최종수정일자
    private String category; // 카테고리
    private List<CnsCareerVO> cnscareerVO; // 약력 VO

}
