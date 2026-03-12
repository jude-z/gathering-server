package api.common.mapper;

import entity.alarm.Alarm;
import entity.user.User;

import java.time.LocalDateTime;

public class AlarmMapper {

    public static Alarm toAlarm(String content, User user) {
        return Alarm.builder()
                .content(content)
                .user(user)
                .date(LocalDateTime.now())
                .checked(false)
                .build();
    }
}
