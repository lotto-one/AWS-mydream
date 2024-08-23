package kr.co.ict.yourdream.vo;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "TBCHATROOM")
public class ChatRoomVO {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_chtno")
    @SequenceGenerator(name = "seq_chtno", sequenceName = "seq_chtno", allocationSize = 1)
    @Column(name = "CHTNO", nullable = false)
    private Long chtno;

    @ManyToOne
    @JoinColumn(name = "memno", referencedColumnName = "memno")
    private MemberVO member;

    @ManyToOne
    @JoinColumn(name = "cnsno", nullable = false)
    private ConsultVO consult;

    @Column(name = "CHATNAME", length = 100)
    private String chatname;

    @Column(name = "CREDT", nullable = false)
    private LocalDateTime credt;

    @Column(name = "UPDDT", nullable = false)
    private LocalDateTime upddt;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.EAGER)
    private List<ChatDetailVO> chatDetails;
}
