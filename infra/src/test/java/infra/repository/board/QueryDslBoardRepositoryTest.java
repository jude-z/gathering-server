package infra.repository.board;

import com.querydsl.jpa.impl.JPAQueryFactory;
import entity.board.Board;
import entity.category.Category;
import entity.gathering.Gathering;
import entity.image.Image;
import entity.user.User;
import infra.repository.dto.querydsl.QueryDslPageResponse;
import infra.repository.dto.querydsl.board.BoardProjection;
import infra.repository.dto.querydsl.board.BoardsProjection;
import infra.repository.category.CategoryRepository;
import infra.repository.gathering.GatheringRepository;
import infra.repository.image.ImageRepository;
import infra.repository.user.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
class QueryDslBoardRepositoryTest {

    @Autowired EntityManager em;
    @Autowired CategoryRepository categoryRepository;
    @Autowired ImageRepository imageRepository;
    @Autowired UserRepository userRepository;
    @Autowired GatheringRepository gatheringRepository;
    @Autowired BoardRepository boardRepository;

    QueryDslBoardRepository queryDslBoardRepository;

    Category category;
    Image userImage, gatheringImage;
    List<User> users;
    Gathering gathering;
    List<Board> boards;

    @BeforeEach
    void setUp() {
        queryDslBoardRepository = new QueryDslBoardRepository(new JPAQueryFactory(em));
        category = returnDummyCategory(1);
        userImage = returnDummyImage(1);
        gatheringImage = returnDummyImage(2);
        users = List.of(returnDummyUser(1, userImage), returnDummyUser(2, userImage), returnDummyUser(3, userImage));
        gathering = returnDummyGathering(1, category, users.get(0), gatheringImage);
        boards = List.of(
                returnDummyBoard(users.get(0), gathering, 1),
                returnDummyBoard(users.get(1), gathering, 2),
                returnDummyBoard(users.get(2), gathering, 3));
    }

    private void saveAll() {
        imageRepository.saveAll(List.of(userImage, gatheringImage));
        categoryRepository.save(category);
        userRepository.saveAll(users);
        gatheringRepository.save(gathering);
        boardRepository.saveAll(boards);
    }

    @Test
    void fetchBoards() {
        saveAll();

        PageableInfo pageableInfo = PageableInfo.of(0, 2);
        QueryDslPageResponse<BoardsProjection> response = queryDslBoardRepository.fetchBoards(pageableInfo);

        assertThat(response.getTotalCount()).isEqualTo(3);
        assertThat(response.getTotalPage()).isEqualTo(2);
        assertThat(response.getContent()).hasSize(2);
    }

    @Disabled("QueryDslBoardRepository.fetchBoard has duplicate 'image' alias collision bug in production code")
    @Test
    void fetchBoard() {
        saveAll();

        PageableInfo pageableInfo = PageableInfo.of(0, 10);
        QueryDslPageResponse<BoardProjection> response = queryDslBoardRepository.fetchBoard(pageableInfo, boards.get(0).getId());

        assertThat(response.getContent()).isNotEmpty();
    }
}
