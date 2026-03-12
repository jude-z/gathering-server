package infra.repository.category;

import com.querydsl.jpa.impl.JPAQueryFactory;
import entity.category.Category;
import entity.gathering.Gathering;
import entity.image.Image;
import entity.user.User;
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
class QueryDslCategoryRepositoryTest {

    @Autowired EntityManager em;
    @Autowired CategoryRepository categoryRepository;
    @Autowired ImageRepository imageRepository;
    @Autowired UserRepository userRepository;
    @Autowired GatheringRepository gatheringRepository;

    QueryDslCategoryRepository queryDslCategoryRepository;

    Category category;
    Image userImage, gatheringImage;
    User user;
    Gathering gathering;

    @BeforeEach
    void setUp() {
        queryDslCategoryRepository = new QueryDslCategoryRepository(new JPAQueryFactory(em));
        category = returnDummyCategory(1);
        userImage = returnDummyImage(1);
        gatheringImage = returnDummyImage(2);
        user = returnDummyUser(1, userImage);
        gathering = returnDummyGathering(1, category, user, gatheringImage);
    }

    @Test
    void findBy() {
        imageRepository.saveAll(List.of(userImage, gatheringImage));
        categoryRepository.save(category);
        userRepository.save(user);
        gatheringRepository.save(gathering);

        Optional<Category> result = queryDslCategoryRepository.findBy(gathering.getId(), "category1");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("category1");
    }
}
