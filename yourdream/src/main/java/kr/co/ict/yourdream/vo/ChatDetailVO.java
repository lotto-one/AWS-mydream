package kr.co.ict.yourdream.vo;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "TBCHATDETAIL")
public class ChatDetailVO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEqNO", nullable = false)
    private Long seqno;

    @ManyToOne
    @JoinColumn(name = "CHTNO", nullable = false)
    private ChatRoomVO chatRoom;

    @Column(name = "CHATDIV", length = 3)
    private String chatdiv;

    @Column(name = "CONTENT", length = 3000)
    private String content;

    @Column(name = "CHATDT")
    private LocalDateTime chatdt;
}
