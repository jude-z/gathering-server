package infra.repository.alarm;

import com.querydsl.jpa.impl.JPAQueryFactory;
import entity.alarm.Alarm;
import entity.image.Image;
import entity.user.User;
import infra.repository.dto.querydsl.QueryDslPageResponse;
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

import static infra.utils.DummyData.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.yml")
class QueryDslAlarmRepositoryTest {

    @Autowired EntityManager em;
    @Autowired AlarmRepository alarmRepository;
    @Autowired UserRepository userRepository;
    @Autowired ImageRepository imageRepository;

    QueryDslAlarmRepository queryDslAlarmRepository;

    Image image;
    User user;
    List<Alarm> uncheckedAlarms;
    List<Alarm> checkedAlarms;

    @BeforeEach
    void setUp() {
        queryDslAlarmRepository = new QueryDslAlarmRepository(new JPAQueryFactory(em));
        image = returnDummyImage(1);
        user = returnDummyUser(1, image);
        uncheckedAlarms = List.of(
                returnDummyAlarm(1, user, false),
                returnDummyAlarm(2, user, false),
                returnDummyAlarm(3, user, false),
                returnDummyAlarm(4, user, false));
        checkedAlarms = List.of(
                returnDummyAlarm(1, user, true),
                returnDummyAlarm(2, user, true),
                returnDummyAlarm(3, user, true),
                returnDummyAlarm(4, user, true));
    }

    @Test
    void findUncheckedAlarm() {
        imageRepository.save(image);
        userRepository.save(user);
        alarmRepository.saveAll(uncheckedAlarms);

        PageableInfo pageableInfo = PageableInfo.of(0, 2);
        QueryDslPageResponse<Alarm> response = queryDslAlarmRepository.findUncheckedAlarm(pageableInfo, user.getId());

        assertThat(response.getTotalCount()).isEqualTo(4);
        assertThat(response.getTotalPage()).isEqualTo(2);
        assertThat(response.getContent()).hasSize(2);
    }

    @Test
    void findCheckedAlarm() {
        imageRepository.save(image);
        userRepository.save(user);
        alarmRepository.saveAll(checkedAlarms);

        PageableInfo pageableInfo = PageableInfo.of(0, 2);
        QueryDslPageResponse<Alarm> response = queryDslAlarmRepository.findCheckedAlarm(pageableInfo, user.getId());

        assertThat(response.getTotalCount()).isEqualTo(4);
        assertThat(response.getTotalPage()).isEqualTo(2);
        assertThat(response.getContent()).hasSize(2);
    }
}
