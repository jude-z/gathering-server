package response.chat.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import spring.myproject.common.functional.MyFunctionalInterface;

@AllArgsConstructor
@Builder
@Data
public class ParticipantElement {
    private Long userId;
    private String username;
    private String nickname;
    private String imageUrl;
    private boolean status;

    public static ParticipantElement from(ParticipantElement participantElement, MyFunctionalInterface myFunctionalInterface) {
        return ParticipantElement.builder()
                .userId(participantElement.getUserId())
                .username(participantElement.getUsername())
                .nickname(participantElement.getNickname())
                .imageUrl(myFunctionalInterface.execute(participantElement.getImageUrl()))
                .status(participantElement.isStatus())
                .build();
    }
}
