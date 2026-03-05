package entity.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.entity.image.Image;
import spring.myproject.entity.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
    @ManyToOne
    @JoinColumn(name = "gathering_id")
    Gathering gathering;
    @OneToMany(mappedBy = "board")
    @Builder.Default
    List<Image> images = new ArrayList<>();
    private String title;
    @Lob
    private String description;
    private LocalDateTime registerDate;
}
