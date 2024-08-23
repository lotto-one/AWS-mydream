package kr.co.ict.yourdream.chat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.ict.yourdream.vo.ChatDetailVO;
import kr.co.ict.yourdream.vo.ChatRoomVO;

@Service

public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatDetailRepository chatDetailRepository;

    public List<ChatRoomVO> getall() {
        return chatRepository.findAll();
    }

    public List<ChatRoomListDTO> getroomlist(Long cnsno) {
        System.out.println(chatRepository.findChatroomsWithMaxChtno(cnsno).get(0).getName());

        return chatRepository.findChatroomsWithMaxChtno(cnsno);

    }

    public List<ChatDetailDTO> getdetail(Long chtno) {

        return chatDetailRepository.findChatDetailsByChtno(chtno);
    }

    public ChatProfileDTO ChatProfile(Long chtno) {
        return chatRepository.findChatProfile(chtno);
    }
}
