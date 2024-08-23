package kr.ict.mydream.chat;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

import kr.ict.mydream.vo.MemberVO;
import kr.ict.mydream.vo.ChatDetailVO;
import kr.ict.mydream.vo.ChatRoomVO;

@Mapper
public interface ChatDao {

    public List<ChatDetailVO> chatDetail(MemberVO vo);

    public void chatSave(ChatDetailVO vo);

    public int chackRoom(int memno);

    public Map<String, String> chatpro(MemberVO vo);

    public int getchatcroom(ChatRoomVO vo);
}
