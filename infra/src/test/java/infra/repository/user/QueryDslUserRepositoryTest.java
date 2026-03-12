package infra.repository.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import entity.category.Category;
import entity.enrollment.Enrollment;
import entity.gathering.Gathering;
import entity.image.Image;
import entity.user.User;
import infra.repository.dto.querydsl.QueryDslPageResponse;
import infra.repository.category.CategoryRepository;
import infra.repository.enrollment.EnrollmentRepository;
import infra.repository.gathering.GatheringRepository;
import infra.repository.image.ImageRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import util.page.PageableInfo;

import java.util.List;
import java.util.Optional;

import static infra.utils.DummyData.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.yml")
class QueryDslUserRepositoryTest {

    @Autowired EntityManager em;
    @Autowired UserRepository userRepository;
    @Autowired ImageRepository imageRepository;
    @Autowired CategoryRepository categoryRepository;
    @Autowired GatheringRepository gatheringRepository;
    @Autowired EnrollmentRepository enrollmentRepository;

    QueryDslUserRepository queryDslUserRepository;

    Image image;
    List<User> users;

    @BeforeEach
    void setUp() {
        queryDslUserRepository = new QueryDslUserRepository(new JPAQueryFactory(em));
        image = returnDummyImage(1);
        users = List.of(returnDummyUser(1, image), returnDummyUser(2, image));
    }

    @Test
    void findByEmail() {
        imageRepository.save(image);
        userRepository.saveAll(users);

        PageableInfo pageableInfo = PageableInfo.of(0, 10);
        QueryDslPageResponse<User> response = queryDslUserRepository.findByEmail(pageableInfo, "email1");

        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().get(0).getEmail()).isEqualTo("email1");
    }

    @Test
    void findByIdFetchImage() {
        imageRepository.save(image);
        userRepository.saveAll(users);

        Optional<User> result = queryDslUserRepository.findByIdFetchImage(users.get(0).getId());

        assertThat(result).isPresent();
        assertThat(result.get().getProfileImage()).isEqualTo(image);
    }

    @Test
    void findByUsername() {
        imageRepository.save(image);
        userRepository.saveAll(users);

        Optional<User> result = queryDslUserRepository.findByUsername("user1");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("user1");
    }

    @Test
    void findAndTokenByUserId() {
        imageRepository.save(image);
        userRepository.saveAll(users);

        Optional<User> result = queryDslUserRepository.findAndTokenByUserId(users.get(0).getId());

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("user1");
    }

    @Test
    void findEnrollmentById() {
        Category category = returnDummyCategory(1);
        Image gatheringImage = returnDummyImage(2);
        Gathering gathering = returnDummyGathering(1, category, users.get(0), gatheringImage);
        List<Enrollment> enrollments = List.of(
                returnDummyEnrollment(users.get(0), gathering),
                returnDummyEnrollment(users.get(1), gathering));
        imageRepository.saveAll(List.of(image, gatheringImage));
        categoryRepository.save(category);
        userRepository.saveAll(users);
        gatheringRepository.save(gathering);
        enrollmentRepository.saveAll(enrollments);

        PageableInfo pageableInfo = PageableInfo.of(0, 10);
        QueryDslPageResponse<User> response = queryDslUserRepository.findEnrollmentById(pageableInfo, gathering.getId(), users.get(0).getId());

        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().get(0).getUsername()).isEqualTo("user2");
    }
}
