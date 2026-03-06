package repository.jpa.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.dto.response.chat.query.ChatMessageElement;
import spring.myproject.entity.chat.ChatMessage;
import spring.myproject.entity.chat.ChatRoom;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("select new spring.myproject.dto.response.chat.query." +
            "ChatMessageElement(cm.id,cr.id,cm.content,uu.id,uu.username,r.status," +
            "case when cp.id=cpp.id then true else false end," +
            "cm.createdAt) " +
            "from ReadStatus r " +
            "left join r.chatParticipant cp " +
            "left join r.chatMessage cm " +
            "left join cp.user u " +
            "left join cm.chatRoom cr " +
            "left join cm.chatParticipant cpp left join cpp.user uu " +
            "where u.id =:userId and cr.id =:chatId and r.status = false"
    )
    List<ChatMessageElement> fetchUnReadMessages(Long chatId, Long userId);

    List<ChatMessage> findChatMessageByChatRoom(ChatRoom chatRoom);
}
