package kr.co.ict.yourdream.userdash;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "tbintres") // 테이블 이름을 지정 (선택 사항)
@Getter
@Setter
public class IntResVOBok { 
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_intno")
    @SequenceGenerator(name = "seq_intno", sequenceName = "seq_intno", allocationSize = 1)
    @Column
    private int intno; // 면접번호(PK)

    // @Column
    // private int memno; // 회원번호(FK)

    @Column
    private String inttypecd; // 직무 or 인성면접

    @Column
    private String cnsfeedbk; // 컨설턴트 피드백(총평)

    @Column
    private float cnsscore; // 컨설턴트 평가점수

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date itenddt; // 면접종료시간

    @Column
    private float reviewscore; // 컨설턴트 평점주기

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date credt; // 최초등록일자

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date upddt; // 최종수정일자

    @ManyToOne
    @JoinColumn(name = "memno", nullable = false)
    private MemberVOBoks member;
}
