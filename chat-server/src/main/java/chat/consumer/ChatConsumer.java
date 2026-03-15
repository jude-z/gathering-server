package chat.consumer;

import infra.kafka.event.Event;
import infra.kafka.event.EventType;
import infra.kafka.event.payload.ChatMessagePayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class ChatConsumer {

    private final SimpMessageSendingOperations messageSendingOperations;
    @KafkaListener(topics = {
            EventType.Topic.CHAT_MESSAGE,
    },
            groupId = "#{T(java.util.UUID).randomUUID().toString()}"
    )
    public void chatMessageListen(String message, Acknowledgment ack) {
        Event<ChatMessagePayload> event = (Event<ChatMessagePayload>)(Event .fromJson(message));
        ChatMessagePayload payload = event.getPayload();
        Long chatRoomId = payload.getChatRoomId();
        messageSendingOperations.convertAndSend("/chatRoom/" + chatRoomId,payload);
        ack.acknowledge();
    }
}
