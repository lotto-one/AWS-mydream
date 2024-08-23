package kr.co.ict.yourdream.chat;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kr.co.ict.yourdream.vo.ChatDetailVO;

@Repository
public interface ChatDetailRepository extends JpaRepository<ChatDetailVO, Long> {

    @Query("SELECT new kr.co.ict.yourdream.chat.ChatDetailDTO(cd.seqno, cd.chatdiv, cd.content, cd.chatdt) " +
            "FROM ChatDetailVO cd " +
            "WHERE cd.chatRoom.chtno = :chtno")
    List<ChatDetailDTO> findChatDetailsByChtno(@Param("chtno") Long chtno);
}
