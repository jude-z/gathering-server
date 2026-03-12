package infra.repository.image;

import com.querydsl.jpa.impl.JPAQueryFactory;
import entity.category.Category;
import entity.gathering.Gathering;
import entity.image.Image;
import entity.user.User;
import infra.repository.dto.querydsl.QueryDslPageResponse;
import infra.repository.category.CategoryRepository;
import infra.repository.gathering.GatheringRepository;
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
class QueryDslImageRepositoryTest {

    @Autowired EntityManager em;
    @Autowired CategoryRepository categoryRepository;
    @Autowired ImageRepository imageRepository;
    @Autowired UserRepository userRepository;
    @Autowired GatheringRepository gatheringRepository;

    QueryDslImageRepository queryDslImageRepository;

    Category category;
    Image userImage, gatheringImage;
    User user;
    Gathering gathering;

    @BeforeEach
    void setUp() {
        queryDslImageRepository = new QueryDslImageRepository(new JPAQueryFactory(em));
        category = returnDummyCategory(1);
        userImage = returnDummyImage(1);
        gatheringImage = returnDummyImage(2);
        user = returnDummyUser(1, userImage);
        gathering = returnDummyGathering(1, category, user, gatheringImage);
    }

    @Test
    void gatheringImage() {
        imageRepository.saveAll(List.of(userImage, gatheringImage));
        categoryRepository.save(category);
        userRepository.save(user);
        gatheringRepository.save(gathering);

        Image img1 = Image.builder().url("img1").gathering(gathering).build();
        Image img2 = Image.builder().url("img2").gathering(gathering).build();
        imageRepository.saveAll(List.of(img1, img2));

        PageableInfo pageableInfo = PageableInfo.of(0, 10);
        QueryDslPageResponse<String> response = queryDslImageRepository.gatheringImage(gathering.getId(), pageableInfo);

        assertThat(response.getContent()).hasSize(2);
        assertThat(response.getContent()).containsExactly("img1", "img2");
    }
}
