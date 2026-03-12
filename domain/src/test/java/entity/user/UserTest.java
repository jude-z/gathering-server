package entity.user;

import entity.image.Image;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private User createUser() {
        return User.builder()
                .username("testUser")
                .password("password123")
                .email("test@test.com")
                .address("Seoul")
                .age(25)
                .hobby("coding")
                .role(Role.USER)
                .nickname("tester")
                .build();
    }

    @Test
    @DisplayName("changeProfileImage - 프로필 이미지 변경")
    void changeProfileImage() {
        User user = createUser();
        Image newImage = Image.builder().url("new-image.png").contentType("image/png").build();

        user.changeProfileImage(newImage);

        assertThat(user.getProfileImage()).isEqualTo(newImage);
    }

    @Test
    @DisplayName("change - 사용자 정보 일괄 변경")
    void change() {
        User user = createUser();

        user.change("newPassword", "new@test.com", "Busan", 30, "reading", "newNick");

        assertThat(user.getPassword()).isEqualTo("newPassword");
        assertThat(user.getEmail()).isEqualTo("new@test.com");
        assertThat(user.getAddress()).isEqualTo("Busan");
        assertThat(user.getAge()).isEqualTo(30);
        assertThat(user.getHobby()).isEqualTo("reading");
        assertThat(user.getNickname()).isEqualTo("newNick");
    }

    @Test
    @DisplayName("change - 기존 값과 무관하게 새 값으로 덮어씀")
    void change_overwritesAllFields() {
        User user = createUser();

        user.change("pw1", "e1@test.com", "addr1", 20, "h1", "n1");
        user.change("pw2", "e2@test.com", "addr2", 40, "h2", "n2");

        assertThat(user.getPassword()).isEqualTo("pw2");
        assertThat(user.getEmail()).isEqualTo("e2@test.com");
        assertThat(user.getAddress()).isEqualTo("addr2");
        assertThat(user.getAge()).isEqualTo(40);
        assertThat(user.getHobby()).isEqualTo("h2");
        assertThat(user.getNickname()).isEqualTo("n2");
    }
}
