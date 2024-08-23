package kr.co.ict.yourdream.chat;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatDetailDTO {

    private Long seqno;
    private String chatdiv;
    private String content;
    private LocalDateTime chatdt;

}