package entity.enrollment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class EnrollmentTest {

    @Test
    @DisplayName("changeAccepted - 미승인 상태에서 승인으로 변경")
    void changeAccepted() {
        Enrollment enrollment = Enrollment.builder()
                .accepted(false)
                .date(LocalDateTime.now())
                .build();

        assertThat(enrollment.isAccepted()).isFalse();

        enrollment.changeAccepted();

        assertThat(enrollment.isAccepted()).isTrue();
    }

    @Test
    @DisplayName("changeAccepted - 이미 승인된 상태에서 호출해도 승인 유지")
    void changeAccepted_alreadyAccepted() {
        Enrollment enrollment = Enrollment.builder()
                .accepted(true)
                .date(LocalDateTime.now())
                .build();

        enrollment.changeAccepted();

        assertThat(enrollment.isAccepted()).isTrue();
    }
}
