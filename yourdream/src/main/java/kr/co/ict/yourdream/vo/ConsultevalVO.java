package kr.co.ict.yourdream.vo;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TBCONSULTEVAL")
public class ConsultevalVO {
    @Id
    @Column
    private Integer intno; // 면접번호(FK)
    @Id
    @Column
    private Integer qno; // 질문번호(FK)
    @Column
    private Integer memno; // 회원번호(FK)
    @Column
    private Integer cnsno; // 컨설턴트 번호(FK)
    @Column
    private String qcnsfeedbk; // 질문에 따른 컨설턴트 피드백
    @Column
    private Float qcnsscore; // 질문에 따른 컨설턴트 부여점수
    @Column
    private Date credt; // 최초등록일자
    @Column
    private Date upddt; // 최종수정일자

    @JoinColumns({
            @JoinColumn(name = "intno"),
            @JoinColumn(name = "qno")
    })
    @ManyToOne(fetch = FetchType.LAZY)
    private IntDetailVO intDetailVO;
}
