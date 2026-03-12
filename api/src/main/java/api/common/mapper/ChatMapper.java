package api.common.mapper;

import entity.chat.ChatParticipant;
import entity.chat.ChatRoom;
import entity.user.User;

public class ChatMapper {

    public static ChatParticipant toChatParticipant(ChatRoom chatRoom, User user, boolean status) {
        return ChatParticipant.builder()
                .chatRoom(chatRoom)
                .user(user)
                .status(status)
                .build();
    }
}
