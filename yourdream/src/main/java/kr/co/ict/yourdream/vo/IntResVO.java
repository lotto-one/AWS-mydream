package kr.co.ict.yourdream.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(IntResVOId.class)
@Table(name = "TBINTRES")
public class IntResVO {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_intno")
    @SequenceGenerator(name = "seq_intno", sequenceName = "seq_intno", allocationSize = 1)
    private Integer intno; // 면접번호(PK)
    @Id
    @Column
    private Integer memno; // 회원번호(FK)
    @Column
    private String inttypecd; // 직무 or 인성면접
    @Column
    private String cnsfeedbk; // 컨설턴트 피드백(총평)
    @Column
    private Float cnsscore; // 컨설턴트 평가점수
    @Column
    private Date reviewdt; // 면접종료시간
    @Column
    private Float reviewscore; // 컨설턴트 평점주기
    @Column
    private Date credt; // 최초등록일자
    @Column
    private Date upddt; // 최종수정일자

    @JoinColumn(name = "memno")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberVO memberVO;

    @OneToMany(mappedBy = "intResVO", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<IntFeedbkVO> feedbkVOList = new ArrayList<>();

    @OneToMany(mappedBy = "intResVO", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<IntDetailVO> detailVOList = new ArrayList<>();
}
