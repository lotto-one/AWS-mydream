package kr.co.ict.yourdream.consultantDashBoard;

import java.util.Date;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TBCONSULT")
@Getter
@Setter
public class ConsultDashBoard {
    @Id
    @Column(name = "CNSNO")
    private int cnsno;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "GENDERCD", length = 1)
    private String gendercd;

    @Column(name = "CATEGCD")
    private String categcd;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "PHONENUM")
    private String phonenum;

    @Column(name = "BIRTHYMD")
    private String birthymd;

    @Column(name = "INTRODUCE", columnDefinition = "TEXT")
    private String introduce;

    @Column(name = "QUITYMD")
    private String quitymd;

    @Column(name = "IMGNAME")
    private String imgname;

    @Column(name = "CNSCAREER")
    private String cnscareer;

    @Column(name = "CNSPROJECT")
    private String cnsproject;

    @Column(name = "ROLECD")
    private String rolecd;

    @Column(name = "CREDT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date credt;

    @Column(name = "UPDDT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date upddt;
}
