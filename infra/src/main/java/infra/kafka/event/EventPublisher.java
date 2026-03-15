package infra.kafka.event;

import infra.kafka.event.payload.ChatMessageCompletePayload;
import infra.kafka.event.payload.ChatMessagePayload;
import infra.redis.generator.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publishEvent(Event<? extends EventPayload> event){
        eventPublisher.publishEvent(event);
    }


}
