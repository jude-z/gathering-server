package util;

import entity.fcm.Topic;
import entity.gathering.Gathering;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class TopicGeneratorTest {

    @Test
    @DisplayName("Gathering으로부터 토픽 이름을 생성한다")
    void generateTopicName() {
        Gathering gathering = createGatheringWithId(1L);

        String topicName = TopicGenerator.generateTopicName(gathering);

        assertThat(topicName).isEqualTo("gathering_1");
    }

    @Test
    @DisplayName("Gathering으로부터 Topic 엔티티를 생성한다")
    void generateTopic() {
        Gathering gathering = createGatheringWithId(5L);

        Topic topic = TopicGenerator.generateTopic(gathering);

        assertThat(topic.getTopicName()).isEqualTo("gathering_5");
        assertThat(topic.getGathering()).isEqualTo(gathering);
    }

    @Test
    @DisplayName("다른 Gathering ID는 다른 토픽 이름을 생성한다")
    void differentGatheringIds() {
        Gathering gathering1 = createGatheringWithId(1L);
        Gathering gathering2 = createGatheringWithId(2L);

        String topic1 = TopicGenerator.generateTopicName(gathering1);
        String topic2 = TopicGenerator.generateTopicName(gathering2);

        assertThat(topic1).isNotEqualTo(topic2);
    }

    private Gathering createGatheringWithId(Long id) {
        Gathering gathering = Gathering.builder().title("test").build();
        try {
            Field idField = Gathering.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(gathering, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return gathering;
    }
}
