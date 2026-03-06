package response.gathering.querydto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GatheringsQuery {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime registerDate;
    private String category;
    private String createdBy;
    private String url;
    private int count;

    public static GatheringsQuery of(MainGatheringsQuery mainGatheringsQuery){
        return GatheringsQuery.builder()
                .id(mainGatheringsQuery.getId())
                .title(mainGatheringsQuery.getTitle())
                .content(mainGatheringsQuery.getContent())
                .registerDate(mainGatheringsQuery.getRegisterDate())
                .category(mainGatheringsQuery.getCategory())
                .createdBy(mainGatheringsQuery.getCreatedBy())
                .url(mainGatheringsQuery.getUrl())
                .count(mainGatheringsQuery.getCount())
                .build();
    }
}
