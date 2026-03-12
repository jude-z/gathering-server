package entity.chat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChatRoomTest {

    @Test
    @DisplayName("changeCount - 참여자 수 변경")
    void changeCount() {
        ChatRoom chatRoom = ChatRoom.builder()
                .title("test room")
                .description("desc")
                .count(0)
                .build();

        chatRoom.changeCount(5);

        assertThat(chatRoom.getCount()).isEqualTo(5);
    }
}
