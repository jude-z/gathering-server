package infra.repository.meeting;

import entity.category.Category;
import entity.gathering.Gathering;
import entity.image.Image;
import entity.meeting.Meeting;
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
class JdbcMeetingRepositoryTest {

    @Autowired EntityManager em;
    @Autowired DataSource dataSource;
    @Autowired CategoryRepository categoryRepository;
    @Autowired ImageRepository imageRepository;
    @Autowired UserRepository userRepository;
    @Autowired GatheringRepository gatheringRepository;
    @Autowired MeetingRepository meetingRepository;

    JdbcMeetingRepository jdbcMeetingRepository;

    Category category;
    Image userImage, gatheringImage, meetingImage;
    User user;
    Gathering gathering;
    Meeting meeting;

    @BeforeEach
    void setUp() {
        jdbcMeetingRepository = new JdbcMeetingRepository(dataSource);
        category = returnDummyCategory(1);
        userImage = returnDummyImage(1);
        gatheringImage = returnDummyImage(2);
        meetingImage = returnDummyImage(3);
        user = returnDummyUser(1, userImage);
        gathering = returnDummyGathering(1, category, user, gatheringImage);
        meeting = returnDummyMeeting(1, user, gathering, meetingImage);
    }

    private void saveAll() {
        imageRepository.saveAll(List.of(userImage, gatheringImage, meetingImage));
        categoryRepository.save(category);
        userRepository.save(user);
        gatheringRepository.save(gathering);
        meetingRepository.save(meeting);
        em.flush();
        em.clear();
    }

    @Test
    void updateCount() {
        saveAll();
        int initialCount = meeting.getCount();

        jdbcMeetingRepository.updateCount(meeting.getId(), 5);

        em.flush();
        em.clear();

        Meeting updated = meetingRepository.findById(meeting.getId()).orElseThrow();
        assertThat(updated.getCount()).isEqualTo(initialCount + 5);
    }
}
