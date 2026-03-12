package infra.kafka;

import infra.kafka.event.Event;
import infra.kafka.event.EventPayload;
import infra.kafka.event.EventType;
import infra.repository.chat.ChatMessageRepository;
import infra.repository.chat.ChatParticipantRepository;
import infra.repository.chat.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final ChatMessageRepository chatMessageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final KafkaProducer kafkaProducer;
    private final RedisTemplate<String,String> redisTemplate;
    @Value("{chat-message.key}")
    private String key;

    @KafkaListener(topics = {
            EventType.Topic.CHAT_MESSAGE,
    })
    public void chatMessageListen(String message, Acknowledgment ack) {
        Event<EventPayload> event = Event.fromJson(message);
        if(event == null) return;
        EventPayload payload = event.getPayload();
        String chatKey = generateKey(payload);
        String chatValue = redisTemplate.opsForValue().get(chatKey);
        if(!StringUtils.hasText(chatValue)) return;

        //db처리


        ack.acknowledge();
    }
    private String generateKey(EventPayload eventPayload){
        Long eventId = eventPayload.getEventId();
        return key + ":" + eventId;
    }
}
