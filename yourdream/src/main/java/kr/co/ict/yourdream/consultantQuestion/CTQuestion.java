package kr.co.ict.yourdream.consultantQuestion;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Builder
@Table(name = "tbcnsquestion")
public class CTQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cnsqno")
    @SequenceGenerator(name = "seq_cnsqno", sequenceName = "seq_cnsqno", allocationSize = 1)
    private Long cnsqno;

    @Column(nullable = false)
    private Long cnsno;

    @Column(length = 3000, nullable = true)
    private String question;

    @Column(length = 50, nullable = true)
    private String keyword1;

    @Column(length = 50, nullable = true)
    private String keyword2;

    @Column(length = 50, nullable = true)
    private String keyword3;

    @Column(nullable = false, columnDefinition = "date default sysdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date credt;

    @Column(nullable = false, columnDefinition = "date default sysdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date upddt;

    public CTQuestion(Long cnsqno, Long cnsno, String question, String keyword1, String keyword2, String keyword3,
            Date credt, Date upddt) {
        this.cnsqno = cnsqno;
        this.cnsno = cnsno;
        this.question = question;
        this.keyword1 = keyword1;
        this.keyword2 = keyword2;
        this.keyword3 = keyword3;
        this.credt = credt;
        this.upddt = upddt;
    }
}
