package kr.ict.mydream.chat;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.ict.mydream.vo.MemberVO;
import kr.ict.mydream.vo.ChatDetailVO;
import kr.ict.mydream.vo.ChatRoomVO;

@Service
public class ChatService {

    @Autowired
    private ChatDao chatDao;

    public List<ChatDetailVO> chatDetail(MemberVO vo) {
        return chatDao.chatDetail(vo);
    }

    public void chatSave(ChatDetailVO vo) {
        chatDao.chatSave(vo);
    }

    public int chackRoom(int memno) {
        return chatDao.chackRoom(memno);
    };

    public Map<String, String> chatpro(MemberVO vo) {
        return chatDao.chatpro(vo);
    }

    public int getchatcroom(ChatRoomVO vo) {
        return chatDao.getchatcroom(vo);
    }
}
