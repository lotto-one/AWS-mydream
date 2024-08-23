package kr.co.ict.yourdream.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TBINTDETAIL")
public class IntDetailVO {
    @Id
    @Column
    private Integer intno; // 면접번호(FK)
    @Id
    @Column
    private Integer qno; // 질문번호(PK)
    @Column
    private Integer aiqno; // AI질문번호
    @Column
    private Integer cnsqno; // 컨설턴트질문번호
    @Column
    private String question; // 질문제목
    @Column
    private String answer; // 질문답변 STT
    @Column
    private Float ecntgood; // 감정긍정count
    @Column
    private Float ecntsoso; // 감정중립count
    @Column
    private Float ecntbad; // 감정부정count
    @Column
    private Float pbadcnt; // 자세불량count
    @Column
    private Float vhertz; // 음성주파수
    @Column
    private Float vjitter; // 음성진폭
    @Column
    private Float vspeed; // 음성공백시간
    @Column
    private String aifeedbk; // AI피드백디테일
    @Column
    private Date credt; // 최초등록일자
    @Column
    private Date upddt; // 최종수정일자
    @Column
    private Float escore; // 감정평균점수 (이걸 기준으로 상중하)
    @Column
    private Float pscore; // 자세평균점수 (이걸 기준으로 상중하)
    @Column
    private Float vscore; // 음성평균점수 (이걸 기준으로 상중하)

    @JoinColumns({
            @JoinColumn(name = "intno"),
            @JoinColumn(name = "memno")
    })
    @ManyToOne(fetch = FetchType.LAZY)
    private IntResVO intResVO;

    @OneToMany(mappedBy = "intDetailVO", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ConsultevalVO> consultevalVOList = new ArrayList<>();

}
