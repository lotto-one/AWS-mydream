package kr.co.ict.yourdream.login.api;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "TBCNSCAREER")
@IdClass(CnsCareerId.class)
public class CnsCareerVO99 {

    @Id
    @Column
    private int cnsno; // 컨설턴트 번호

    @Id
    @Column
    private int seqno; // 시퀀스 번호


    
    @Column
    private String term; // 약력기간
    
    @Column
    private String content; // 약력내용
    
    @Column
    private String detail; // 약력디테일
    
    @Column
    private String careerdiv; // 학력/경력구분
    
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date credt; // 최초등록일자
    
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date upddt; // 최종수정일자

    @PrePersist
    protected void onCreate() {
        this.credt = new Date();
        this.upddt = new Date();
    }
    

    
}
