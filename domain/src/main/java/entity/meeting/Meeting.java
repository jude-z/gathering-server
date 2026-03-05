package entity.meeting;

import spring.myproject.entity.attend.Attend;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.entity.image.Image;
import spring.myproject.entity.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static spring.myproject.dto.request.meeting.MeetingRequestDto.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "meeting")
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private LocalDateTime meetingDate;
    private LocalDateTime endDate;
    @Lob
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User createdBy;
    @OneToMany(mappedBy = "meeting",cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<Attend> attends = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_id")
    private Gathering gathering;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image image;
    private int count;

    public void attend(List<Attend> attends){
        for (Attend attend : attends) {
            attend.addMeeting(this);
        }
        this.attends = attends;
    }
    public static Meeting of(AddMeetingRequest addMeetingRequest, Image image, User user, Gathering gathering){
        return Meeting.builder()
                .title(addMeetingRequest.getTitle())
                .content(addMeetingRequest.getContent())
                .createdBy(user)
                .meetingDate(addMeetingRequest.getMeetingDate())
                .endDate(addMeetingRequest.getEndDate())
                .gathering(gathering)
                .count(1)
                .image(image)
                .build();
    }
}
