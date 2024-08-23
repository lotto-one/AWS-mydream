package kr.co.ict.yourdream.chat;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import kr.co.ict.yourdream.vo.ChatRoomVO;

@Repository
@Transactional
public interface ChatRepository extends JpaRepository<ChatRoomVO, Long> {

        @Query("SELECT new kr.co.ict.yourdream.chat.ChatRoomListDTO(memno,chtno, name,imgname) " +
                        "FROM ChatRoomVO c JOIN c.member m WHERE c.chtno = (" +
                        "    SELECT MAX(c2.chtno)     FROM ChatRoomVO c2 " +
                        "    WHERE c2.member.memno = c.member.memno " +
                        "    AND c2.consult.cnsno = :cnsno" +
                        ") " +
                        "AND m.memno IN (" +
                        "    SELECT mc.memno FROM MemberConsultVO mc " +
                        "    WHERE mc.cnsno = :cnsno AND mc.useyn = 'Y'" +
                        ") ORDER BY memno ASC")
        List<ChatRoomListDTO> findChatroomsWithMaxChtno(@Param("cnsno") Long cnsno);

        @Query("SELECT new kr.co.ict.yourdream.chat.ChatProfileDTO(" +
                        "m.nickname, m.imgname, c.name, c.imgname) " +
                        "FROM ChatRoomVO cr " +
                        "JOIN cr.member m " +
                        "JOIN cr.consult c " +
                        "WHERE cr.chtno = :chtno")
        ChatProfileDTO findChatProfile(@Param("chtno") Long chtno);
}