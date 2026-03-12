package infra.repository.chat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import entity.category.Category;
import entity.chat.ChatMessage;
import entity.chat.ChatParticipant;
import entity.chat.ChatRoom;
import entity.chat.ReadStatus;
import entity.gathering.Gathering;
import entity.image.Image;
import entity.user.User;
import infra.repository.dto.querydsl.QueryDslPageResponse;
import infra.repository.category.CategoryRepository;
import infra.repository.dto.querydsl.chat.*;
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
import java.util.Optional;

import static infra.utils.DummyData.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.yml")
class QueryDslChatRepositoryTest {

    @Autowired EntityManager em;
    @Autowired CategoryRepository categoryRepository;
    @Autowired ImageRepository imageRepository;
    @Autowired UserRepository userRepository;
    @Autowired GatheringRepository gatheringRepository;
    @Autowired ChatRoomRepository chatRoomRepository;
    @Autowired ChatParticipantRepository chatParticipantRepository;
    @Autowired ChatMessageRepository chatMessageRepository;
    @Autowired ReadStatusRepository readStatusRepository;

    QueryDslChatRepository queryDslChatRepository;

    Category category;
    Image userImage, gatheringImage;
    List<User> users;
    Gathering gathering;
    ChatRoom chatRoom1, chatRoom2;
    List<ChatParticipant> participants;
    List<ChatMessage> messages;
    List<ReadStatus> readStatuses;

    @BeforeEach
    void setUp() {
        queryDslChatRepository = new QueryDslChatRepository(new JPAQueryFactory(em));
        category = returnDummyCategory(1);
        userImage = returnDummyImage(1);
        gatheringImage = returnDummyImage(2);
        users = List.of(
                returnDummyUser(1, userImage),
                returnDummyUser(2, userImage),
                returnDummyUser(3, userImage));
        gathering = returnDummyGathering(1, category, users.get(0), gatheringImage);
    }

    private void saveBase() {
        imageRepository.saveAll(List.of(userImage, gatheringImage));
        categoryRepository.save(category);
        userRepository.saveAll(users);
        gatheringRepository.save(gathering);
    }

    private void setupChatData() {
        saveBase();

        chatRoom1 = returnDummyChatRoom(users.get(0), gathering, 1);
        chatRoom2 = returnDummyChatRoom(users.get(0), gathering, 2);
        chatRoomRepository.saveAll(List.of(chatRoom1, chatRoom2));

        ChatParticipant p1 = returnDummyChatParticipant(users.get(0), chatRoom1);
        ChatParticipant p2 = returnDummyChatParticipant(users.get(1), chatRoom1);
        ChatParticipant p3 = returnDummyChatParticipant(users.get(2), chatRoom2);
        participants = List.of(p1, p2, p3);
        chatParticipantRepository.saveAll(participants);

        ChatMessage m1 = returnDummyChatMessage(chatRoom1, p1, 1);
        ChatMessage m2 = returnDummyChatMessage(chatRoom1, p2, 2);
        messages = List.of(m1, m2);
        chatMessageRepository.saveAll(messages);

        ReadStatus rs1 = returnDummyReadStatus(p1, m1);
        ReadStatus rs2 = returnDummyReadStatus(p1, m2);
        ReadStatus rs3 = returnDummyReadStatus(p2, m1);
        ReadStatus rs4 = returnDummyReadStatus(p2, m2);
        readStatuses = List.of(rs1, rs2, rs3, rs4);
        readStatusRepository.saveAll(readStatuses);

        em.flush();
        em.clear();
    }

    @Test
    void fetchUnReadMessages() {
        setupChatData();

        PageableInfo pageableInfo = PageableInfo.of(0, 10);
        QueryDslPageResponse<ChatMessageProjection> response =
                queryDslChatRepository.fetchUnReadMessages(pageableInfo, chatRoom1.getId(), users.get(0).getId());

        assertThat(response.getContent()).hasSize(2);
    }

    @Test
    void fetchParticipant() {
        setupChatData();

        PageableInfo pageableInfo = PageableInfo.of(0, 10);
        QueryDslPageResponse<ParticipantProjection> response =
                queryDslChatRepository.fetchParticipant(pageableInfo, chatRoom1.getId(), users.get(0).getId());

        assertThat(response.getContent()).hasSize(2);
        assertThat(response.getContent().get(0).getUsername()).isEqualTo("user1");
    }

    @Test
    void fetchMyChatRooms() {
        setupChatData();

        PageableInfo pageableInfo = PageableInfo.of(0, 10);
        QueryDslPageResponse<MyChatRoomProjection> response =
                queryDslChatRepository.fetchMyChatRooms(pageableInfo, users.get(0).getId());

        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().get(0).getChatRoomTitle()).isEqualTo("title1");
    }

    @Test
    void fetchChatRooms() {
        setupChatData();

        PageableInfo pageableInfo = PageableInfo.of(0, 10);
        QueryDslPageResponse<ChatRoomProjection> response =
                queryDslChatRepository.fetchChatRooms(pageableInfo, users.get(0).getId(), gathering.getId());

        assertThat(response.getContent()).hasSize(2);
    }

    @Test
    void fetchAbleChatRooms() {
        setupChatData();

        PageableInfo pageableInfo = PageableInfo.of(0, 10);
        QueryDslPageResponse<AbleChatRoomProjection> response =
                queryDslChatRepository.fetchAbleChatRooms(pageableInfo, users.get(2).getId(), gathering.getId());

        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().get(0).getChatRoomTitle()).isEqualTo("title1");
    }

    @Test
    void fetchParticipateChatRooms() {
        setupChatData();

        PageableInfo pageableInfo = PageableInfo.of(0, 10);
        QueryDslPageResponse<ParticipateChatRoomProjection> response =
                queryDslChatRepository.fetchParticipateChatRooms(pageableInfo, users.get(0).getId(), gathering.getId());

        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().get(0).getChatRoomTitle()).isEqualTo("title1");
    }

    @Test
    void fetchChatRoomById() {
        setupChatData();

        Optional<ChatRoom> result = queryDslChatRepository.fetchChatRoomById(chatRoom1.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("title1");
        assertThat(result.get().getCreatedBy().getUsername()).isEqualTo("user1");
    }
}
