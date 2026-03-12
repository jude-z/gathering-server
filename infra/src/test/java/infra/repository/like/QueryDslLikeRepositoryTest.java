package infra.repository.like;

import com.querydsl.jpa.impl.JPAQueryFactory;
import entity.category.Category;
import entity.gathering.Gathering;
import entity.image.Image;
import entity.like.Like;
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
class QueryDslLikeRepositoryTest {

    @Autowired EntityManager em;
    @Autowired CategoryRepository categoryRepository;
    @Autowired ImageRepository imageRepository;
    @Autowired UserRepository userRepository;
    @Autowired GatheringRepository gatheringRepository;
    @Autowired LikeRepository likeRepository;

    QueryDslLikeRepository queryDslLikeRepository;

    Category category;
    Image userImage, gatheringImage;
    List<User> users;
    Gathering gathering;
    List<Like> likes;

    @BeforeEach
    void setUp() {
        queryDslLikeRepository = new QueryDslLikeRepository(new JPAQueryFactory(em));
        category = returnDummyCategory(1);
        userImage = returnDummyImage(1);
        gatheringImage = returnDummyImage(2);
        users = List.of(returnDummyUser(1, userImage), returnDummyUser(2, userImage));
        gathering = returnDummyGathering(1, category, users.get(0), gatheringImage);
        likes = List.of(returnDummyLike(users.get(1), gathering));
    }

    @Test
    void findLike() {
        imageRepository.saveAll(List.of(userImage, gatheringImage));
        categoryRepository.save(category);
        userRepository.saveAll(users);
        gatheringRepository.save(gathering);
        likeRepository.saveAll(likes);

        Optional<Like> result = queryDslLikeRepository.findLike(users.get(1).getId(), gathering.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getLikedBy()).isEqualTo(users.get(1));
    }
}
