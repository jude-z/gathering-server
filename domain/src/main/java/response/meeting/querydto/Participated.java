package response.meeting.querydto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class Participated {
    private Long id;
    private String profileImageUrl;
}
