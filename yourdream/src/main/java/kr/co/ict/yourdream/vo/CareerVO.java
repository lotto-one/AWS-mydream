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
@Table(name = "TBCAREER")
public class CareerVO {
    @Id
    private int rsmno;
    @Id
    private int seqno;
    @Column
    private String entymd;
    @Column
    private String quitymd;
    @Column
    private String compname;
    @Column
    private String jobclass;
    @Column
    private String maintask;
    @Column
    private String jobposition;
    @Column
    private Date credt;
    @Column
    private Date upddt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rsmno")
    private ResumeVO resumeVO;
}
