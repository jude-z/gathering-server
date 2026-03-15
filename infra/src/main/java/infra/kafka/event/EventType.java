package infra.kafka.event;

import infra.kafka.event.payload.ChatMessageCompletePayload;
import infra.kafka.event.payload.ChatMessagePayload;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum EventType {
    CHAT_MESSAGE(ChatMessagePayload.class, Topic.CHAT_MESSAGE);

    private final Class<? extends EventPayload> payloadClass;
    private final String topic;

    public static class Topic {
        public static final String CHAT_MESSAGE = "chat-message";
        public static final String CHAT_MESSAGE_COMPLETE = "chat-message-complete";
    }
}
