package entity.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.myproject.entity.chat.ChatMessage;
import spring.myproject.entity.chat.ChatParticipant;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "read_status")
public class ReadStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_participant_id")
    private ChatParticipant chatParticipant;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_message_id")
    private ChatMessage chatMessage;
}
