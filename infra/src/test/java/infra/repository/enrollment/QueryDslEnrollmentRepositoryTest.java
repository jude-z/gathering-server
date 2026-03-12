package infra.repository.enrollment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import entity.category.Category;
import entity.enrollment.Enrollment;
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

import java.util.List;
import java.util.Optional;

import static infra.utils.DummyData.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.yml")
class QueryDslEnrollmentRepositoryTest {

    @Autowired EntityManager em;
    @Autowired CategoryRepository categoryRepository;
    @Autowired ImageRepository imageRepository;
    @Autowired UserRepository userRepository;
    @Autowired GatheringRepository gatheringRepository;
    @Autowired EnrollmentRepository enrollmentRepository;

    QueryDslEnrollmentRepository queryDslEnrollmentRepository;

    Category category;
    Image userImage, gatheringImage;
    List<User> users;
    Gathering gathering;
    List<Enrollment> enrollments;

    @BeforeEach
    void setUp() {
        queryDslEnrollmentRepository = new QueryDslEnrollmentRepository(new JPAQueryFactory(em));
        category = returnDummyCategory(1);
        userImage = returnDummyImage(1);
        gatheringImage = returnDummyImage(2);
        users = List.of(returnDummyUser(1, userImage), returnDummyUser(2, userImage), returnDummyUser(3, userImage));
        gathering = returnDummyGathering(1, category, users.get(0), gatheringImage);
        enrollments = List.of(
                returnDummyEnrollment(users.get(0), gathering),
                returnDummyEnrollment(users.get(1), gathering),
                returnDummyEnrollment(users.get(2), gathering));
    }

    private void saveAll() {
        imageRepository.saveAll(List.of(userImage, gatheringImage));
        categoryRepository.save(category);
        userRepository.saveAll(users);
        gatheringRepository.save(gathering);
        enrollmentRepository.saveAll(enrollments);
    }

    @Test
    void existEnrollment() {
        saveAll();

        Enrollment result = queryDslEnrollmentRepository.existEnrollment(gathering.getId(), users.get(0).getId());

        assertThat(result).isNotNull();
        assertThat(result.getEnrolledBy()).isEqualTo(users.get(0));
    }

    @Test
    void findEnrollment() {
        saveAll();

        Optional<Enrollment> result = queryDslEnrollmentRepository.findEnrollment(gathering.getId(), users.get(1).getId(), true);

        assertThat(result).isPresent();
        assertThat(result.get().getEnrolledBy()).isEqualTo(users.get(1));
    }

    @Test
    void findEnrollmentEnrolledByAndTokensById() {
        Enrollment unapproved = Enrollment.builder()
                .date(java.time.LocalDateTime.now())
                .accepted(false)
                .gathering(gathering)
                .enrolledBy(users.get(0))
                .build();
        imageRepository.saveAll(List.of(userImage, gatheringImage));
        categoryRepository.save(category);
        userRepository.saveAll(users);
        gatheringRepository.save(gathering);
        enrollmentRepository.save(unapproved);

        Optional<Enrollment> result = queryDslEnrollmentRepository.findEnrollmentEnrolledByAndTokensById(unapproved.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getEnrolledBy()).isEqualTo(users.get(0));
    }
}
