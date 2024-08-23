package kr.ict.mydream.chat;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.ict.mydream.vo.ChatDetailVO;
import kr.ict.mydream.vo.MemberVO;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/detail")
    public List<ChatDetailVO> chatDeatil(@RequestBody MemberVO vo) {

        System.out.println("chatdetail 접속");
        System.out.println(vo.getMemno());
        return chatService.chatDetail(vo);
    }

    @PostMapping("/save")
    public void chatSave(@RequestBody ChatDetailVO vo) {
        chatService.chatSave(vo);
    }

    @PostMapping("/chatpro")
    public Map<String, String> chatProfile(@RequestBody MemberVO vo) {
        System.out.println(chatService.chatpro(vo));
        return chatService.chatpro(vo);
    }
}
