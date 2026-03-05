package entity.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.myproject.entity.user.User;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "chat_participant")
public class ChatParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;
    private boolean status;

    public void changeStatus(boolean status) {
        this.status = status;
    }

    public static ChatParticipant of(ChatRoom chatRoom,User user,boolean status){
        return ChatParticipant.builder()
                .chatRoom(chatRoom)
                .user(user)
                .status(status)
                .build();
    }


}
