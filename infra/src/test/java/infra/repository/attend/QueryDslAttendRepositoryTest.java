package infra.repository.attend;

import com.querydsl.jpa.impl.JPAQueryFactory;
import entity.attend.Attend;
import entity.category.Category;
import entity.gathering.Gathering;
import entity.image.Image;
import entity.meeting.Meeting;
import entity.user.User;
import infra.repository.dto.querydsl.QueryDslPageResponse;
import infra.repository.category.CategoryRepository;
import infra.repository.enrollment.EnrollmentRepository;
import infra.repository.gathering.GatheringRepository;
import infra.repository.image.ImageRepository;
import infra.repository.meeting.MeetingRepository;
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
class QueryDslAttendRepositoryTest {

    @Autowired EntityManager em;
    @Autowired CategoryRepository categoryRepository;
    @Autowired ImageRepository imageRepository;
    @Autowired UserRepository userRepository;
    @Autowired GatheringRepository gatheringRepository;
    @Autowired EnrollmentRepository enrollmentRepository;
    @Autowired MeetingRepository meetingRepository;
    @Autowired AttendRepository attendRepository;

    QueryDslAttendRepository queryDslAttendRepository;

    Category category;
    Image userImage, gatheringImage, meetingImage;
    List<User> users;
    Gathering gathering;
    List<Meeting> meetings;
    List<Attend> attends;

    @BeforeEach
    void setUp() {
        queryDslAttendRepository = new QueryDslAttendRepository(new JPAQueryFactory(em));
        category = returnDummyCategory(1);
        userImage = returnDummyImage(1);
        gatheringImage = returnDummyImage(2);
        meetingImage = returnDummyImage(3);
        users = List.of(returnDummyUser(1, userImage), returnDummyUser(2, userImage), returnDummyUser(3, userImage));
        gathering = returnDummyGathering(1, category, users.get(0), gatheringImage);
        meetings = List.of(returnDummyMeeting(1, users.get(0), gathering, meetingImage));
        Meeting meeting = meetings.get(0);
        attends = List.of(
                returnDummyAttend(users.get(0), meeting),
                returnDummyAttend(users.get(1), meeting),
                returnDummyAttend(users.get(2), meeting));
    }

    @Test
    void findByUserIdAndMeetingId() {
        imageRepository.saveAll(List.of(userImage, gatheringImage, meetingImage));
        categoryRepository.save(category);
        userRepository.saveAll(users);
        gatheringRepository.save(gathering);
        meetingRepository.saveAll(meetings);
        attendRepository.saveAll(attends);

        User user1 = users.get(0);
        Meeting meeting = meetings.get(0);
        PageableInfo pageableInfo = PageableInfo.of(0, 10);

        QueryDslPageResponse<Attend> response = queryDslAttendRepository.findByUserIdAndMeetingId(pageableInfo, user1.getId(), meeting.getId());

        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().get(0).getAttendBy()).isEqualTo(user1);
    }
}
