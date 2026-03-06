package repository.querydsl.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.dto.response.chat.query.AbleChatRoomElement;
import spring.myproject.dto.response.chat.query.ChatRoomElement;
import spring.myproject.dto.response.chat.query.MyChatRoomElement;
import spring.myproject.dto.response.chat.query.ParticipateChatRoomElement;
import spring.myproject.entity.chat.ChatRoom;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("select new spring.myproject.dto.response.chat.query." +
            "MyChatRoomElement(c.id,c.title,c.description,c.count,cr.username,true," +
            "(select count(r.id) from ReadStatus r " +
            "left join r.chatParticipant cp " +
            "where cp.chatRoom.id = c.id and r.status=false and cp.user.id = u.id),g.title," +
            "(select cm1.content from ReadStatus r1 " +
            "left join r1.chatParticipant cp1 " +
            "left join r1.chatMessage cm1 " +
            "where r1.status = false and cp1.chatRoom.id = c.id and cp1.user.id = u.id " +
            "order by r1.id desc limit 1)," +
            "(select cm2.createdAt from ReadStatus r2 " +
            "left join r2.chatParticipant cp2 " +
            "left join r2.chatMessage cm2 " +
            "where r2.status = false and cp2.chatRoom.id = c.id and cp2.user.id = u.id " +
            "order by r2.id desc limit 1)) " +
            "from ChatParticipant p " +
            "left join p.chatRoom c " +
            "left join c.gathering g " +
            "left join p.user u " +
            "left join c.createdBy cr " +
            "where u.id = :userId " +
            "group by c.id")
    Page<MyChatRoomElement> fetchMyChatRooms(Pageable pageable, Long userId);

    @Query("select new spring.myproject.dto.response.chat.query." +
            "ChatRoomElement(c.id, c.title,c.description, c.count, u.username, " +
            "case when cp.id is not null then true else false end," +
            "case when cp.id is not null then " +
            "(select count(r.id) from ReadStatus r " +
            "left join r.chatParticipant cpp " +
            "where cpp.chatRoom.id = c.id and r.status=false and cpp.user.id = u.id) " +
            "else 0 end," +
            "g.title," +
            "(select cm1.content from ReadStatus r1 " +
            "left join r1.chatParticipant cp1 " +
            "left join r1.chatMessage cm1 " +
            "where r1.status = false and cp1.chatRoom.id = c.id and cp1.user.id = u.id " +
            "order by r1.id desc limit 1)," +
            "(select cm2.createdAt from ReadStatus r2 " +
            "left join r2.chatParticipant cp2 " +
            "left join r2.chatMessage cm2 " +
            "where r2.status = false and cp2.chatRoom.id = c.id and cp2.user.id = u.id " +
            "order by r2.id desc limit 1)) " +
            "from ChatRoom c left join c.gathering g " +
            "left join c.createdBy u " +
            "left join ChatParticipant cp on cp.chatRoom.id = c.id and cp.user.id = :userId " +
            "where g.id = :gatheringId " +
            "order by case when cp.id is not null then 0 else 1 end, c.id asc")
    Page<ChatRoomElement> fetchChatRooms(Pageable pageable, Long userId,Long gatheringId);
    @Query("select new spring.myproject.dto.response.chat.query." +
            "AbleChatRoomElement(c.id, c.title,c.description, c.count, u.username,g.title)" +
            "from ChatRoom c left join c.gathering g " +
            "left join c.createdBy u " +
            "left join ChatParticipant cp on cp.chatRoom.id = c.id and cp.user.id = :userId " +
            "where cp.id is null and g.id = :gatheringId " +
            "order by c.id asc")
    Page<AbleChatRoomElement> fetchAbleChatRooms(Pageable pageable, Long userId,Long gatheringId);
    @Query("select new spring.myproject.dto.response.chat.query." +
            "ParticipateChatRoomElement(c.id, c.title,c.description, c.count, u.username, " +
            "case when cp.id is not null then " +
            "(select count(r.id) from ReadStatus r " +
            "left join r.chatParticipant cpp where cpp.chatRoom.id = c.id and r.status=false and cpp.user.id = u.id) " +
            "else 0 end," +
            "g.title," +
            "(select cm1.content from ReadStatus r1 " +
            "left join r1.chatParticipant cp1 " +
            "left join r1.chatMessage cm1 " +
            "where r1.status = false and cp1.chatRoom.id = c.id and cp1.user.id = u.id " +
            "order by r1.id desc limit 1)," +
            "(select cm2.createdAt from ReadStatus r2 " +
            "left join r2.chatParticipant cp2 " +
            "left join r2.chatMessage cm2 " +
            "where r2.status = false and cp2.chatRoom.id = c.id and cp2.user.id = u.id " +
            "order by r2.id desc limit 1)) " +
            "from ChatRoom c left join c.gathering g " +
            "left join c.createdBy u " +
            "left join ChatParticipant cp on cp.chatRoom.id = c.id and cp.user.id = :userId " +
            "where cp.id is not null and g.id = :gatheringId " +
            "order by c.id asc")
    Page<ParticipateChatRoomElement> fetchParticipateChatRooms(Pageable pageable, Long userId,Long gatheringId);

    @Query("select c from ChatRoom c left join fetch c.createdBy u where c.id =:chatRoomId")
    Optional<ChatRoom> fetchChatRoomById(Long chatRoomId);
}
