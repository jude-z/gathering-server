package response.meeting.querydto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import spring.myproject.dto.response.meeting.querydto.MeetingsQueryInterface;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeetingsQuery {

    private Long id;
    private String title;
    private String createdBy;
    private String createdByNickname;
    private LocalDateTime meetingDate;
    private LocalDateTime endDate;
    private String content;
    private int count;
    private String url;
    private String participatedImageUrl;
    private Long participatedId;

    public static MeetingsQuery from(MeetingsQueryInterface query, String url){
        return MeetingsQuery.builder()
                .id(query.getId())
                .title(query.getTitle())
                .createdBy(query.getCreatedBy())
                .createdByNickname(query.getCreatedByNickname())
                .meetingDate(query.getMeetingDate())
                .endDate(query.getEndDate())
                .content(query.getContent())
                .count(query.getCount())
                .url(url+query.getUrl())
                .participatedImageUrl(url+query.getParticipatedImageUrl())
                .participatedId(query.getParticipatedId())
                .build();
    }
}
