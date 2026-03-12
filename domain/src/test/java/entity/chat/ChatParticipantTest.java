package entity.chat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChatParticipantTest {

    @Test
    @DisplayName("changeStatus - 비활성에서 활성으로 변경")
    void changeStatus_toActive() {
        ChatParticipant participant = ChatParticipant.builder()
                .status(false)
                .build();

        participant.changeStatus(true);

        assertThat(participant.isStatus()).isTrue();
    }

    @Test
    @DisplayName("changeStatus - 활성에서 비활성으로 변경")
    void changeStatus_toInactive() {
        ChatParticipant participant = ChatParticipant.builder()
                .status(true)
                .build();

        participant.changeStatus(false);

        assertThat(participant.isStatus()).isFalse();
    }
}
