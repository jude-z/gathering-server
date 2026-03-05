package entity.like;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.entity.user.User;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "likes")
@AllArgsConstructor
@Builder
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User likedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_id")
    private Gathering gathering;

    public static Like of(Gathering gathering, User user) {
        return Like.builder()
                .gathering(gathering)
                .likedBy(user)
                .build();
    }
}
