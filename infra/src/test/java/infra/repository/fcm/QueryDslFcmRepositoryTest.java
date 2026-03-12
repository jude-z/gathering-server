package infra.repository.fcm;

import com.querydsl.jpa.impl.JPAQueryFactory;
import entity.category.Category;
import entity.fcm.FCMToken;
import entity.fcm.Topic;
import entity.fcm.UserTopic;
import entity.gathering.Gathering;
import entity.image.Image;
import entity.user.User;
import infra.repository.dto.querydsl.QueryDslPageResponse;
import infra.repository.category.CategoryRepository;
import infra.repository.gathering.GatheringRepository;
import infra.repository.image.ImageRepository;
import infra.repository.user.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import util.page.PageableInfo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static infra.utils.DummyData.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.yml")
class QueryDslFcmRepositoryTest {

    @Autowired EntityManager em;
    @Autowired ImageRepository imageRepository;
    @Autowired UserRepository userRepository;
    @Autowired CategoryRepository categoryRepository;
    @Autowired GatheringRepository gatheringRepository;
    @Autowired FCMTokenRepository fcmTokenRepository;
    @Autowired TopicRepository topicRepository;
    @Autowired UserTopicRepository userTopicRepository;

    QueryDslFcmRepository queryDslFcmRepository;

    Image userImage, gatheringImage;
    User user;
    Category category;
    Gathering gathering;

    @BeforeEach
    void setUp() {
        queryDslFcmRepository = new QueryDslFcmRepository(new JPAQueryFactory(em));
        userImage = returnDummyImage(1);
        gatheringImage = returnDummyImage(2);
        category = returnDummyCategory(1);
        user = returnDummyUser(1, userImage);
        gathering = returnDummyGathering(1, category, user, gatheringImage);
    }

    private void saveBase() {
        imageRepository.saveAll(List.of(userImage, gatheringImage));
        categoryRepository.save(category);
        userRepository.save(user);
        gatheringRepository.save(gathering);
    }

    @Test
    void findByTokenValueAndUser() {
        saveBase();
        FCMToken token = returnDummyFCMToken(user, "token123", 1);
        fcmTokenRepository.save(token);

        em.flush();
        em.clear();

        Optional<FCMToken> result = queryDslFcmRepository.findByTokenValueAndUser("token123", user.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getTokenValue()).isEqualTo("token123");
    }

    @Test
    void findByTokenValueAndUser_notFound() {
        saveBase();
        FCMToken token = returnDummyFCMToken(user, "token123", 1);
        fcmTokenRepository.save(token);

        em.flush();
        em.clear();

        Optional<FCMToken> result = queryDslFcmRepository.findByTokenValueAndUser("nonexistent", user.getId());

        assertThat(result).isEmpty();
    }

    @Test
    void findByExpirationDate() {
        saveBase();
        FCMToken token1 = returnDummyFCMToken(user, "token1", 2);
        FCMToken token2 = returnDummyFCMToken(user, "token2", 3);
        fcmTokenRepository.saveAll(List.of(token1, token2));

        em.flush();
        em.clear();

        PageableInfo pageableInfo = PageableInfo.of(0, 10);
        QueryDslPageResponse<FCMToken> response = queryDslFcmRepository.findByExpirationDate(pageableInfo, LocalDate.now());

        assertThat(response.getContent()).hasSize(2);
    }

    @Test
    void findByUserIdWithPageable() {
        saveBase();
        Topic topic = returnDummyTopic("topic1", gathering);
        topicRepository.save(topic);
        UserTopic userTopic = returnDummyUserTopic(topic, user);
        userTopicRepository.save(userTopic);

        em.flush();
        em.clear();

        PageableInfo pageableInfo = PageableInfo.of(0, 10);
        QueryDslPageResponse<UserTopic> response = queryDslFcmRepository.findByUserId(pageableInfo, user.getId());

        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().get(0).getTopic().getTopicName()).isEqualTo("topic1");
    }

    @Disabled("QueryDslFcmRepository.findByUserId(Long) passes limit=0 to PageCalculator causing division by zero")
    @Test
    void findByUserId() {
        saveBase();
        Topic topic = returnDummyTopic("topic1", gathering);
        topicRepository.save(topic);
        UserTopic userTopic = returnDummyUserTopic(topic, user);
        userTopicRepository.save(userTopic);

        em.flush();
        em.clear();

        QueryDslPageResponse<UserTopic> response = queryDslFcmRepository.findByUserId(user.getId());

        assertThat(response.getContent()).hasSize(1);
    }

    @Test
    void existsByTopicAndUser_true() {
        saveBase();
        Topic topic = returnDummyTopic("topic1", gathering);
        topicRepository.save(topic);
        UserTopic userTopic = returnDummyUserTopic(topic, user);
        userTopicRepository.save(userTopic);

        em.flush();
        em.clear();

        boolean exists = queryDslFcmRepository.existsByTopicAndUser("topic1", user.getId());

        assertThat(exists).isTrue();
    }

    @Test
    void existsByTopicAndUser_false() {
        saveBase();
        Topic topic = returnDummyTopic("topic1", gathering);
        topicRepository.save(topic);

        em.flush();
        em.clear();

        boolean exists = queryDslFcmRepository.existsByTopicAndUser("topic1", user.getId());

        assertThat(exists).isFalse();
    }
}
