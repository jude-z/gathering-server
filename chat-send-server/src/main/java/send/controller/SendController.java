package send.controller;


import infra.kafka.KafkaProducer;
import infra.kafka.event.*;
import infra.kafka.event.payload.ChatMessageCompletePayload;
import infra.kafka.event.payload.ChatMessagePayload;
import infra.redis.generator.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class SendController {

    private final KafkaProducer kafkaProducer;
    private final IdGenerator idGenerator;
    private final OutboxRepository outboxRepository;
    private final EventPublisher eventPublisher;
    private final SimpMessageSendingOperations messageSendingOperations;

    @MessageMapping("/{chatRoomId}")
    public void sendMessage(@DestinationVariable Long chatRoomId, @Payload ChatMessagePayload chatMessagePayload){
        Event event = createChatMessageEvent(chatRoomId, chatMessagePayload);
        Outbox outbox = Outbox.create(event, LocalDateTime.now());
        outboxRepository.save(outbox);
        eventPublisher.publishEvent(event);
        messageSendingOperations.convertAndSend("/chatRoom/" + chatRoomId,chatMessagePayload);
    }

    public Event createChatMessageEvent(Long chatRoomId, ChatMessagePayload chatMessagePayload) {
        Long chatSendId = idGenerator.nextIdForReply();

        chatMessagePayload.setPartitionKey(chatRoomId);
        chatMessagePayload.setEventId(chatSendId);
        return Event.builder()
                .payload(chatMessagePayload)
                .type(EventType.CHAT_MESSAGE)
                .build();
    }
}
