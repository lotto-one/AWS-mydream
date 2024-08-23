package kr.co.ict.yourdream.vo;

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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TBRESUME")
public class ResumeVO {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_rsmno")
    @SequenceGenerator(name = "seq_rsmno", sequenceName = "seq_rsmno", allocationSize = 1)
    private int rsmno;
    @Column
    private String title;
    @Column
    private String name;
    @Column
    private String imgname;
    @Column
    private String addr;
    @Column
    private String birthymd;
    @Column
    private String email;
    @Column
    private String mphonenum;
    @Column
    private String hphonenum;
    @Column
    private Date credt;
    @Column
    private Date upddt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memno")
    private MemberVO memberVO;

    @OneToMany(mappedBy = "resumeVO", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<EducationVO> educationVOList = new ArrayList<>();

    @OneToMany(mappedBy = "resumeVO", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CareerVO> careerVOList = new ArrayList<>();

    @OneToMany(mappedBy = "resumeVO", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<SelfIntroVO> selfIntroVOList = new ArrayList<>();
}
