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
@Table(name = "TBEDUCATION")
public class EducationVO {
    @Id
    private int seqno;
    @Id
    private int rsmno;
    @Column
    private String entymd;
    @Column
    private String gradeymd;
    @Column
    private String schoolname;
    @Column
    private String major;
    @Column
    private String gradeuateyn;
    @Column
    private String gradescore;
    @Column
    private Date credt;
    @Column
    private Date upddt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rsmno")
    private ResumeVO resumeVO;
}
