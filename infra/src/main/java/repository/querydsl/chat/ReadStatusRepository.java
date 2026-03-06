package repository.querydsl.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.entity.chat.ReadStatus;

import java.util.List;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, Long> {
    @Query("update ReadStatus r set r.status = true " +
            "where r.chatParticipant.id = :chatParticipantId and r.chatMessage.id in :chatMessageIds and r.status=false")
    @Modifying
    void readChatMessage(Long chatParticipantId, List<Long> chatMessageIds);

}
