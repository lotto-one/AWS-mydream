package kr.co.ict.yourdream.chat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.ict.yourdream.vo.ChatDetailVO;
import kr.co.ict.yourdream.vo.ChatRoomVO;

@RestController
@RequestMapping("/consultant/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @GetMapping("/getlist")
    public List<ChatRoomListDTO> chatroomlist(@RequestParam("cnsno") Long cnsno) {

        return chatService.getroomlist(cnsno);
    }

    @GetMapping("/chatdetail")
    public List<ChatDetailDTO> getdetail(@RequestParam("chtno") Long chtno) {
        return chatService.getdetail(chtno);
    }

    @GetMapping("/profile")
    public ChatProfileDTO getprofile(@RequestParam("chtno") Long chtno) {
        return chatService.ChatProfile(chtno);
    }

}
