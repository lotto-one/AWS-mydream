package kr.co.ict.yourdream.vo;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(MemberConsultVOId.class)
@Table(name = "TBMEMBERCONSULT")
public class MemberConsultVO {

    @Id
    private int memno; // 컨설턴트번호(FK)
    @Id
    private int cnsno; // 회원번호(FK)
    @Column
    private String useyn; // 사용여부
    @Column
    private Date credt; // 최초등록일자
    @Column
    private Date upddt; // 최초수정일자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memno")
    private MemberVO memberVO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cnsno")
    private ConsultVO consultVO;
}
