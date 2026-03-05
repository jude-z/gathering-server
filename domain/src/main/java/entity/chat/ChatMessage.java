package entity.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.myproject.entity.chat.ChatParticipant;

import java.time.LocalDateTime;

import static spring.myproject.dto.request.chat.ChatRequestDto.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "chat_message")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_participant_id")
    private ChatParticipant chatParticipant;
    private LocalDateTime createdAt;

    public static ChatMessage of(ChatRoom chatRoom, ChatParticipant chatParticipant, ChatMessageRequest chatMessageRequest){
        return ChatMessage.builder()
                .chatRoom(chatRoom)
                .chatParticipant(chatParticipant)
                .content(chatMessageRequest.getContent())
                .build();
    }

}
