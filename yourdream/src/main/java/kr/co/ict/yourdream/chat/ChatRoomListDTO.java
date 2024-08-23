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
public class ChatRoomListDTO {

    private Integer memno;
    private Long chtno;
    private String name;
    private String imgname;
}
