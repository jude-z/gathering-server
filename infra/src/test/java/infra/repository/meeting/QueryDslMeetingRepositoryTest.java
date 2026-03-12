package infra.repository.meeting;

import com.querydsl.jpa.impl.JPAQueryFactory;
import entity.attend.Attend;
import entity.category.Category;
import entity.gathering.Gathering;
import entity.image.Image;
import entity.meeting.Meeting;
import entity.user.User;
import infra.repository.dto.querydsl.QueryDslPageResponse;
import infra.repository.dto.querydsl.meeting.MeetingProjection;
import infra.repository.dto.querydsl.meeting.MeetingsProjection;
import infra.repository.attend.AttendRepository;
import infra.repository.category.CategoryRepository;
import infra.repository.enrollment.EnrollmentRepository;
import infra.repository.gathering.GatheringRepository;
import infra.repository.image.ImageRepository;
import infra.repository.user.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import util.page.PageableInfo;

import java.util.List;

import static infra.utils.DummyData.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.yml")
class QueryDslMeetingRepositoryTest {

    @Autowired EntityManager em;
    @Autowired CategoryRepository categoryRepository;
    @Autowired ImageRepository imageRepository;
    @Autowired UserRepository userRepository;
    @Autowired GatheringRepository gatheringRepository;
    @Autowired EnrollmentRepository enrollmentRepository;
    @Autowired MeetingRepository meetingRepository;
    @Autowired AttendRepository attendRepository;

    QueryDslMeetingRepository queryDslMeetingRepository;

    Category category;
    Image userImage, gatheringImage, meetingImage;
    List<User> users;
    Gathering gathering;
    List<Meeting> meetings;
    List<Attend> attends;

    @BeforeEach
    void setUp() {
        queryDslMeetingRepository = new QueryDslMeetingRepository(new JPAQueryFactory(em));
        category = returnDummyCategory(1);
        userImage = returnDummyImage(1);
        gatheringImage = returnDummyImage(2);
        meetingImage = returnDummyImage(3);
        users = List.of(
                returnDummyUser(1, userImage), returnDummyUser(2, userImage),
                returnDummyUser(3, userImage), returnDummyUser(4, userImage));
        gathering = returnDummyGathering(1, category, users.get(0), gatheringImage);
        meetings = List.of(
                returnDummyMeeting(1, users.get(0), gathering, meetingImage),
                returnDummyMeeting(2, users.get(3), gathering, meetingImage));
        Meeting m1 = meetings.get(0);
        Meeting m2 = meetings.get(1);
        attends = List.of(
                returnDummyAttend(users.get(0), m1),
                returnDummyAttend(users.get(1), m1),
                returnDummyAttend(users.get(2), m1),
                returnDummyAttend(users.get(3), m2));
    }

    private void saveAll() {
        imageRepository.saveAll(List.of(userImage, gatheringImage, meetingImage));
        categoryRepository.save(category);
        userRepository.saveAll(users);
        gatheringRepository.save(gathering);
        meetingRepository.saveAll(meetings);
        attendRepository.saveAll(attends);
    }

    @Test
    void meetingDetail() {
        saveAll();

        QueryDslPageResponse<MeetingProjection> response = queryDslMeetingRepository.meetingDetail(meetings.get(0).getId());

        assertThat(response.getContent()).hasSize(3);
        assertThat(response.getContent()).extracting("attendedBy")
                .containsExactly("user1", "user2", "user3");
    }

    @Test
    void meetings() {
        saveAll();

        PageableInfo pageableInfo = PageableInfo.of(0, 10);
        QueryDslPageResponse<MeetingsProjection> response = queryDslMeetingRepository.meetings(pageableInfo, gathering.getId());

        assertThat(response.getContent()).hasSizeGreaterThanOrEqualTo(4);
    }
}
