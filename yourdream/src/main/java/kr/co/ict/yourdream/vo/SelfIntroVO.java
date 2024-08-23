package kr.co.ict.yourdream.vo;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
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
@Table(name = "TBSELFINTRO")
public class SelfIntroVO {
    @Id
    private int seqno;
    @Id
    private int rsmno;
    @Column
    private String title;
    @Column
    private String content;
    @Column
    private Date credt;
    @Column
    private Date upddt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rsmno")
    private ResumeVO resumeVO;
}
