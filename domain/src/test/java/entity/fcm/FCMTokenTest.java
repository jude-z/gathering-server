package entity.fcm;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class FCMTokenTest {

    @Test
    @DisplayName("changeExpirationDate - 현재 날짜 기준으로 만료일 재계산")
    void changeExpirationDate() {
        FCMToken token = FCMToken.builder()
                .tokenValue("test-token")
                .expirationDate(LocalDate.of(2025, 1, 1))
                .build();

        token.changeExpirationDate(3);

        LocalDate expected = LocalDate.now().plusMonths(3);
        assertThat(token.getExpirationDate()).isEqualTo(expected);
    }

    @Test
    @DisplayName("changeExpirationDate - 0개월 연장 시 현재 날짜로 설정")
    void changeExpirationDate_zeroMonths() {
        FCMToken token = FCMToken.builder()
                .tokenValue("test-token")
                .expirationDate(LocalDate.of(2025, 1, 1))
                .build();

        token.changeExpirationDate(0);

        assertThat(token.getExpirationDate()).isEqualTo(LocalDate.now());
    }
}
