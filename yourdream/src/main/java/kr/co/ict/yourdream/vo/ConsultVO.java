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
@Table(name = "TBCONSULT")
public class ConsultVO {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cnsno")
    @SequenceGenerator(name = "seq_cnsno", sequenceName = "seq_cnsno", allocationSize = 1)
    private int cnsno; // 컨설턴트 번호(PK)
    @Column
    private String name; // 컨설턴트 성명
    @Column
    private String gendercd; // 컨설턴트 성별
    @Column
    private String categcd; // 전문분야
    @Column
    private String email; // 컨설턴트이메일
    @Column
    private String phonenum; // 컨설턴트 휴대폰번호
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
    private String cnsproject; // 프로젝트수행
    @Column
    private Date credt; // 최초등록일자
    @Column
    private Date upddt; // 최종수정일자
    @Column
    private String category; // 카테고리

    @OneToMany(mappedBy = "consultVO", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<MemberConsultVO> memberConsultVOList = new ArrayList<>();

}
