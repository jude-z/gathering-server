package response.meeting.querydto;

import java.time.LocalDateTime;

public interface MeetingsQueryInterface {

    Long getId();
    String getTitle();
    String getCreatedBy();
    String getCreatedByNickname();
    LocalDateTime getMeetingDate();
    LocalDateTime getEndDate();
    String getContent();
    int getCount();
    String getUrl();
    Long getParticipatedId();
    String getParticipatedImageUrl();
}
