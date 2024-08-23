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
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TBMEMBER")
public class MemberVO {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_memno")
    @SequenceGenerator(name = "seq_memno", sequenceName = "seq_memno", allocationSize = 1)
    private Integer memno;
    @Column
    private String name;
    @Column
    private String gendercd;
    @Column
    private String nickname;
    @Column
    private String categcd;
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private String phonenum;
    @Column
    private String birthymd;
    @Column
    private String loccd;
    @Column
    private String seasoncd;
    @Column
    private String entymd;
    @Column
    private String quitymd;
    @Column
    private String imgname;
    @Column
    private Date credt;
    @Column
    private Date upddt;
    @Column
    private String rolecd;

    @OneToMany(mappedBy = "memberVO", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<IntResVO> intResVOList = new ArrayList<>();

    @OneToMany(mappedBy = "memberVO", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<MemberConsultVO> memberConsultVOList = new ArrayList<>();

    @OneToMany(mappedBy = "memberVO", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ResumeVO> resumeVOList = new ArrayList<>();

}
