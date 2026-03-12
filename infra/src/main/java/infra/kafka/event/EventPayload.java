package infra.kafka.event;

import lombok.Getter;

@Getter
public abstract class EventPayload {
    private String partitionKey;
    private Long eventId;
}
