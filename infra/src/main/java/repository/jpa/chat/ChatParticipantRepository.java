package repository.jpa.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.dto.response.chat.query.ParticipantElement;
import spring.myproject.entity.chat.ChatParticipant;
import spring.myproject.entity.chat.ChatRoom;
import spring.myproject.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {

    Optional<ChatParticipant> findByChatRoomAndUserAndStatus(ChatRoom chatRoom, User user,boolean status);
    List<ChatParticipant> findAllByChatRoomAndStatus(ChatRoom chatRoom, boolean status);
    @Query("select new spring.myproject.dto.response.chat.query.ParticipantElement" +
            "(u.id,u.username,u.nickname,i.url,cp.status) " +
            "from ChatParticipant cp " +
            "left join cp.chatRoom c " +
            "left join cp.user u " +
            "left join u.profileImage i " +
            "where c.id = :chatId " +
            "order by case when u.id =:userId then 0 else 1 end ")
    Page<ParticipantElement> fetchParticipant(Long chatId, Long userId, Pageable pageable);
}
