package infra.repository.gathering;

import entity.category.Category;
import entity.enrollment.Enrollment;
import entity.gathering.Gathering;
import entity.image.Image;
import entity.user.User;
import infra.repository.dto.jdbc.gathering.GatheringDetailProjection;
import infra.repository.dto.jdbc.gathering.MainGatheringsProjection;
import infra.repository.category.CategoryRepository;
import infra.repository.enrollment.EnrollmentRepository;
import infra.repository.image.ImageRepository;
import infra.repository.user.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
class JdbcGatheringRepositoryTest {

    @Autowired EntityManager em;
    @Autowired DataSource dataSource;
    @Autowired CategoryRepository categoryRepository;
    @Autowired ImageRepository imageRepository;
    @Autowired UserRepository userRepository;
    @Autowired GatheringRepository gatheringRepository;
    @Autowired EnrollmentRepository enrollmentRepository;

    JdbcGatheringRepository jdbcGatheringRepository;

    Category category;
    Image userImage, gatheringImage;
    List<User> users;
    Gathering gathering;

    @BeforeEach
    void setUp() {
        jdbcGatheringRepository = new JdbcGatheringRepository(dataSource);
        category = returnDummyCategory(1);
        userImage = returnDummyImage(1);
        gatheringImage = returnDummyImage(2);
        users = List.of(returnDummyUser(1, userImage), returnDummyUser(2, userImage));
        gathering = returnDummyGathering(1, category, users.get(0), gatheringImage);
    }

    private void saveAll() {
        imageRepository.saveAll(List.of(userImage, gatheringImage));
        categoryRepository.save(category);
        userRepository.saveAll(users);
        gatheringRepository.save(gathering);
        em.flush();
        em.clear();
    }

    @Test
    void updateCount() {
        saveAll();
        int initialCount = gathering.getCount();

        jdbcGatheringRepository.updateCount(gathering.getId(), 3);

        em.flush();
        em.clear();

        Gathering updated = gatheringRepository.findById(gathering.getId()).orElseThrow();
        assertThat(updated.getCount()).isEqualTo(initialCount + 3);
    }

    @Disabled("Raw SQL references 'user' reserved keyword - MySQL-specific query incompatible with H2")
    @Test
    void gatheringDetail() {
        saveAll();
        Enrollment enrollment = returnDummyEnrollment(users.get(1), gathering);
        enrollmentRepository.save(enrollment);
        em.flush();
        em.clear();

        List<GatheringDetailProjection> result = jdbcGatheringRepository.gatheringDetail(gathering.getId());

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getTitle()).isEqualTo("title1");
        assertThat(result.get(0).getCategory()).isEqualTo("category1");
    }

    @Disabled("Raw SQL references 'user' reserved keyword and 'rownum' - MySQL-specific query incompatible with H2")
    @Test
    void gatherings() {
        saveAll();

        List<MainGatheringsProjection> result = jdbcGatheringRepository.gatherings();

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getTitle()).isEqualTo("title1");
    }

    @Disabled("Raw SQL references 'user' reserved keyword - MySQL-specific query incompatible with H2")
    @Test
    void subGatherings() {
        saveAll();

        List<MainGatheringsProjection> result = jdbcGatheringRepository.subGatherings("category1");

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getCategory()).isEqualTo("category1");
    }
}
