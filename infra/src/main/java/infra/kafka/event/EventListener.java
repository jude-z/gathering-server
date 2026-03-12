package infra.kafka.event;

import infra.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventListener {
    private final OutboxRepository outboxRepository;
    private final KafkaProducer kafkaProducer;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void createOutbox(Event<EventPayload> event) {
        Outbox outbox = Outbox.create(event, LocalDateTime.now());
        outboxRepository.save(outbox);
    }

    @Async("publishEventExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishEvent(Event<EventPayload> event) {
        kafkaProducer.publishEvent(event);
    }

}
