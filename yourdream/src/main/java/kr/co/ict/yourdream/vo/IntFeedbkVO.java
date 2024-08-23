package kr.co.ict.yourdream.vo;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TBINTFEEDBK")
public class IntFeedbkVO {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_feedno")
    @SequenceGenerator(name = "seq_feedno", sequenceName = "seq_feedno", allocationSize = 1)
    private Integer feedno;
    @Id
    @Column
    private Integer intno;
    @Column
    private String efeed1;
    @Column
    private String efeed2;
    @Column
    private String pfeed1;
    @Column
    private String pfeed2;
    @Column
    private String vfeed1;
    @Column
    private String vfeed2;
    @Column
    private String sttfeed1;
    @Column
    private String sttfeed2;
    @Column
    private Date credt;
    @Column
    private Date upddt;

    @JoinColumns({
            @JoinColumn(name = "intno"),
            @JoinColumn(name = "memno")
    })
    @ManyToOne(fetch = FetchType.LAZY)
    private IntResVO intResVO;
}
