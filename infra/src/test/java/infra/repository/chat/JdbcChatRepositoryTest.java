package infra.repository.chat;

import entity.category.Category;
import entity.chat.ChatMessage;
import entity.chat.ChatParticipant;
import entity.chat.ChatRoom;
import entity.chat.ReadStatus;
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

import javax.sql.DataSource;
import java.util.List;

import static infra.utils.DummyData.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.yml")
class JdbcChatRepositoryTest {

    @Autowired EntityManager em;
    @Autowired DataSource dataSource;
    @Autowired CategoryRepository categoryRepository;
    @Autowired ImageRepository imageRepository;
    @Autowired UserRepository userRepository;
    @Autowired GatheringRepository gatheringRepository;
    @Autowired ChatRoomRepository chatRoomRepository;
    @Autowired ChatParticipantRepository chatParticipantRepository;
    @Autowired ChatMessageRepository chatMessageRepository;
    @Autowired ReadStatusRepository readStatusRepository;

    JdbcChatRepository jdbcChatRepository;

    Category category;
    Image userImage, gatheringImage;
    User user;
    Gathering gathering;
    ChatRoom chatRoom;
    ChatParticipant participant;
    List<ChatMessage> messages;
    List<ReadStatus> readStatuses;

    @BeforeEach
    void setUp() {
        jdbcChatRepository = new JdbcChatRepository(dataSource);
        category = returnDummyCategory(1);
        userImage = returnDummyImage(1);
        gatheringImage = returnDummyImage(2);
        user = returnDummyUser(1, userImage);
        gathering = returnDummyGathering(1, category, user, gatheringImage);
    }

    private void saveAll() {
        imageRepository.saveAll(List.of(userImage, gatheringImage));
        categoryRepository.save(category);
        userRepository.save(user);
        gatheringRepository.save(gathering);

        chatRoom = returnDummyChatRoom(user, gathering, 1);
        chatRoomRepository.save(chatRoom);

        participant = returnDummyChatParticipant(user, chatRoom);
        chatParticipantRepository.save(participant);

        ChatMessage m1 = returnDummyChatMessage(chatRoom, participant, 1);
        ChatMessage m2 = returnDummyChatMessage(chatRoom, participant, 2);
        messages = List.of(m1, m2);
        chatMessageRepository.saveAll(messages);

        ReadStatus rs1 = returnDummyReadStatus(participant, m1);
        ReadStatus rs2 = returnDummyReadStatus(participant, m2);
        readStatuses = List.of(rs1, rs2);
        readStatusRepository.saveAll(readStatuses);

        em.flush();
        em.clear();
    }

    @Test
    void readChatMessage() {
        saveAll();

        List<Long> messageIds = List.of(messages.get(0).getId(), messages.get(1).getId());
        jdbcChatRepository.readChatMessage(participant.getId(), messageIds);

        em.flush();
        em.clear();

        List<ReadStatus> updated = readStatusRepository.findAll();
        assertThat(updated).allMatch(ReadStatus::getStatus);
    }

    @Test
    void readChatMessage_emptyList() {
        saveAll();

        jdbcChatRepository.readChatMessage(participant.getId(), List.of());

        List<ReadStatus> unchanged = readStatusRepository.findAll();
        assertThat(unchanged).allMatch(rs -> !rs.getStatus());
    }
}
