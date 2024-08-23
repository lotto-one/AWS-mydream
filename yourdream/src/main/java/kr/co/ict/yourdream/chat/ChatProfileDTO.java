package kr.co.ict.yourdream.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatProfileDTO {

    private String unick;
    private String uimg;
    private String cname;
    private String cimg;
}
