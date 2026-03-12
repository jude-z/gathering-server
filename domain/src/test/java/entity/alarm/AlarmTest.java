package entity.alarm;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class AlarmTest {

    @Test
    @DisplayName("setChecked - 미확인 알람을 확인 처리")
    void setChecked() {
        Alarm alarm = Alarm.builder()
                .content("test alarm")
                .date(LocalDateTime.now())
                .checked(false)
                .build();

        assertThat(alarm.isChecked()).isFalse();

        alarm.setChecked();

        assertThat(alarm.isChecked()).isTrue();
    }

    @Test
    @DisplayName("setChecked - 이미 확인된 알람에 호출해도 확인 유지")
    void setChecked_alreadyChecked() {
        Alarm alarm = Alarm.builder()
                .content("test alarm")
                .date(LocalDateTime.now())
                .checked(true)
                .build();

        alarm.setChecked();

        assertThat(alarm.isChecked()).isTrue();
    }
}
