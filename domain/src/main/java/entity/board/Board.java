package entity.board;

import entity.gathering.Gathering;
import entity.image.Image;
import entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "gathering_id")
    private Gathering gathering;
    @OneToMany(mappedBy = "board")
    private List<Image> images = new ArrayList<>();
    private String title;
    @Lob
    private String description;
    private LocalDateTime registerDate;
}
