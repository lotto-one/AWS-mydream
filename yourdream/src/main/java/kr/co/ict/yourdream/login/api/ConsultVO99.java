package kr.co.ict.yourdream.login.api;


import lombok.Getter;
import lombok.Setter;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;


@Getter
@Setter
@Entity
@Table(name = "TBCONSULT")
public class ConsultVO99 {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cnsno")
    @SequenceGenerator(name = "seq_cnsno", sequenceName = "seq_cnsno", allocationSize = 1)
    @Column
    private int cnsno; // 컨설턴트 번호(PK)
    
    @Column
    private String name; // 컨설턴트 성명
    
    @Column
    private String gendercd; // 컨설턴트 성별
    
    @Column
    private String categcd; // 전문분야
    
    @Column
    private String email; // 컨설턴트 이메일
    
    @Column
    private String phonenum; // 컨설턴트 휴대폰 번호
    
    @Column
    private String birthymd; // 컨설턴트 생년월일
    
    @Column
    private String introduce; // 컨설턴트 자기소개
    
    @Column
    private String quitymd; // 컨설턴트 퇴사일자
    
    @Column
    private String imgname; // 사진이름
    
    @Column
    private String cnscareer; // 컨설팅경력
    
    @Column
    private String cnsproject; // 프로젝트 수행
    
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date credt; // 최초등록일자
    
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date upddt; // 최종수정일자
    
    @Column
    private String password;

    @Column
    private String rolecd;


    @PrePersist
    protected void onCreate() {
        this.credt = new Date(); // 현재 날짜와 시간으로 설정
        this.upddt = new Date();
    }
}
