package kr.co.ict.yourdream.consultantProfile;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "TBCNSCAREER")
@IdClass(CnsCareerPK.class)
@Getter
@Setter
public class CnsCareer {
    @Id
    @Column(name = "CNSNO")
    private int cnsno;

    @Id
    @Column(name = "SEQNO")
    private int seqno;

    @ManyToOne
    @JoinColumn(name = "CNSNO", insertable = false, updatable = false)
    private ConsultProfile consultProfile;

    @Column(name = "TERM")
    private String term;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "DETAIL")
    private String detail;

    @Column(name = "CAREERDIV")
    private String careerdiv;

    @Column(name = "CREDT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date credt;

    @Column(name = "UPDDT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date upddt;
}