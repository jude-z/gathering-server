package entity.meeting;

import entity.image.Image;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class MeetingTest {

    @Test
    @DisplayName("changeMeeting - 모임 정보 전체 변경")
    void changeMeeting() {
        Meeting meeting = Meeting.builder()
                .title("original")
                .content("original content")
                .meetingDate(LocalDateTime.of(2025, 1, 1, 10, 0))
                .endDate(LocalDateTime.of(2025, 1, 1, 12, 0))
                .build();

        LocalDateTime newMeetingDate = LocalDateTime.of(2025, 6, 1, 14, 0);
        LocalDateTime newEndDate = LocalDateTime.of(2025, 6, 1, 16, 0);
        Image newImage = Image.builder().url("meeting.png").contentType("image/png").build();

        meeting.changeMeeting("new title", "new content", newMeetingDate, newEndDate, newImage);

        assertThat(meeting.getTitle()).isEqualTo("new title");
        assertThat(meeting.getContent()).isEqualTo("new content");
        assertThat(meeting.getMeetingDate()).isEqualTo(newMeetingDate);
        assertThat(meeting.getEndDate()).isEqualTo(newEndDate);
        assertThat(meeting.getImage()).isEqualTo(newImage);
    }

    @Test
    @DisplayName("changeMeeting - 이미지를 null로 변경 가능")
    void changeMeeting_nullImage() {
        Image originalImage = Image.builder().url("original.png").contentType("image/png").build();
        Meeting meeting = Meeting.builder()
                .title("title")
                .content("content")
                .meetingDate(LocalDateTime.of(2025, 1, 1, 10, 0))
                .endDate(LocalDateTime.of(2025, 1, 1, 12, 0))
                .image(originalImage)
                .build();

        meeting.changeMeeting("title", "content",
                LocalDateTime.of(2025, 1, 1, 10, 0),
                LocalDateTime.of(2025, 1, 1, 12, 0),
                null);

        assertThat(meeting.getImage()).isNull();
    }
}
