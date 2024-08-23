package kr.co.ict.yourdream.useradmin;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "tbmemberconsult")
@IdClass(MemberConsultIdBok.class)
public class MemberConsultVOBok {

    @Column
    @Id
    private int cnsno; // 회원번호(FK)

    @Column
    @Id
    private int memno; // 컨설턴트번호(FK)

    @Column
    private String useyn; // 사용여부

    @Column
    private Date credt; // 최초등록일자

    @Column
    private Date upddt; // 최초수정일자


}
