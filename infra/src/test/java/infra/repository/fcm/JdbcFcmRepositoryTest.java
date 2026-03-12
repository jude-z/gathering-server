package infra.repository.fcm;

import entity.category.Category;
import entity.fcm.FCMToken;
import entity.fcm.Topic;
import entity.fcm.UserTopic;
import entity.gathering.Gathering;
import entity.image.Image;
import entity.user.User;
import infra.repository.category.CategoryRepository;
import infra.repository.gathering.GatheringRepository;
import infra.repository.image.ImageRepository;
import infra.repository.user.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.util.List;

import static infra.utils.DummyData.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.yml")
class JdbcFcmRepositoryTest {

    @Autowired EntityManager em;
    @Autowired DataSource dataSource;
    @Autowired ImageRepository imageRepository;
    @Autowired UserRepository userRepository;
    @Autowired CategoryRepository categoryRepository;
    @Autowired GatheringRepository gatheringRepository;
    @Autowired FCMTokenRepository fcmTokenRepository;
    @Autowired TopicRepository topicRepository;
    @Autowired UserTopicRepository userTopicRepository;

    JdbcFcmRepository jdbcFcmRepository;

    Image userImage, gatheringImage;
    User user;
    Category category;
    Gathering gathering;

    @BeforeEach
    void setUp() {
        jdbcFcmRepository = new JdbcFcmRepository(dataSource);
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
    void deleteTokenByTokenValueIn() {
        saveBase();
        FCMToken token1 = returnDummyFCMToken(user, "token1", 1);
        FCMToken token2 = returnDummyFCMToken(user, "token2", 2);
        fcmTokenRepository.saveAll(List.of(token1, token2));
        em.flush();
        em.clear();

        jdbcFcmRepository.deleteTokenByTokenValueIn(List.of("token1"));

        em.flush();
        em.clear();

        List<FCMToken> remaining = fcmTokenRepository.findAll();
        assertThat(remaining).hasSize(1);
        assertThat(remaining.get(0).getTokenValue()).isEqualTo("token2");
    }

    @Test
    void deleteTokenByTokenValueIn_emptyList() {
        saveBase();
        FCMToken token = returnDummyFCMToken(user, "token1", 1);
        fcmTokenRepository.save(token);
        em.flush();
        em.clear();

        jdbcFcmRepository.deleteTokenByTokenValueIn(List.of());

        List<FCMToken> remaining = fcmTokenRepository.findAll();
        assertThat(remaining).hasSize(1);
    }

    @Test
    void deleteUserTopicByTopicAndUser() {
        saveBase();
        Topic topic = returnDummyTopic("topic1", gathering);
        topicRepository.save(topic);
        UserTopic userTopic = returnDummyUserTopic(topic, user);
        userTopicRepository.save(userTopic);
        em.flush();
        em.clear();

        Topic savedTopic = topicRepository.findByTopicName("topic1").orElseThrow();
        User savedUser = userRepository.findById(user.getId()).orElseThrow();

        jdbcFcmRepository.deleteUserTopicByTopicAndUser(savedTopic, savedUser);

        em.flush();
        em.clear();

        List<UserTopic> remaining = userTopicRepository.findAll();
        assertThat(remaining).isEmpty();
    }
}
