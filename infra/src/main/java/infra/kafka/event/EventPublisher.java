package infra.kafka.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

@RequiredArgsConstructor
public class EventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publishEvent(Event<EventPayload> event){
        eventPublisher.publishEvent(event);
    }


}
