package entity.gathering;

import spring.myproject.entity.category.Category;
import spring.myproject.entity.enrollment.Enrollment;
import spring.myproject.entity.fcm.Topic;
import spring.myproject.entity.image.Image;
import spring.myproject.entity.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static spring.myproject.dto.request.gathering.GatheringRequestDto.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "gathering")
@AllArgsConstructor
@Builder
public class Gathering {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Lob
    private String content;
    private LocalDateTime registerDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User createBy;

    private int count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image gatheringImage;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "gathering")
    @Builder.Default
    private List<Enrollment> enrollments = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private Topic topic;

    public void changeTopic(Topic topic){
        this.topic = topic;
    }
    public void changeGathering(Image image, UpdateGatheringRequest updateGatheringRequest){
        if(image != null) this.gatheringImage = image;
        this.title = updateGatheringRequest.getTitle();
        this.content = updateGatheringRequest.getContent();
        this.registerDate = LocalDateTime.now();
    }


    public static Gathering of(AddGatheringRequest addGatheringRequest, User createBy, Image image,Category category){
        return Gathering.builder()
                .title(addGatheringRequest.getTitle())
                .content(addGatheringRequest.getContent())
                .createBy(createBy)
                .registerDate(LocalDateTime.now())
                .gatheringImage(image)
                .category(category)
                .count(1)
                .build();
    }

}
