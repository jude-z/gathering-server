package response.gathering.querydto;

import java.time.LocalDateTime;


public interface MainGatheringsQuery {

    Long getId();
    String getTitle();
    String getContent();
    LocalDateTime getRegisterDate();
    String getCategory();
    String getCreatedBy();
    String getUrl();
    int getCount();
}
