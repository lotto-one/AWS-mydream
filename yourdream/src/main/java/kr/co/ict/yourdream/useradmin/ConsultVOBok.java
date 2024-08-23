package kr.co.ict.yourdream.useradmin;

import lombok.Getter;
import lombok.Setter;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;


@Getter
@Setter
@Entity
@Table(name = "tbconsult") // 실제 테이블 이름에 따라 변경 필요
public class ConsultVOBok {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cnsno")
    @SequenceGenerator(name = "seq_cnsno", sequenceName = "seq_cnsno", allocationSize = 1)
    @Column
    private int cnsno; 

    @Column
    private String name; 

    @Column
    private String gendercd; 

    @Column
    private String categcd; 

    @Column
    private String email; 

    @Column
    private String phonenum; 

    @Column
    private String birthymd; 

    @Column
    private String introduce; 

    @Column
    private String quitymd; 

    @Column
    private String imgname; 

    @Column
    private String cnscareer; 

    @Column
    private String cnsproject; 

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "credt", nullable = false)
    private Date credt; 

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "upddt")
    private Date upddt; 

    @Column(name = "category")
    private String category; 
}