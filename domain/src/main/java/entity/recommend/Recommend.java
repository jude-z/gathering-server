package entity.recommend;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.myproject.entity.gathering.Gathering;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "recommend")
public class Recommend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_id")
    private Gathering gathering;
    private Long score;
    private LocalDate date;

    public static Recommend from(Gathering gathering){
        return Recommend.builder()
                .gathering(gathering)
                .date(LocalDate.now())
                .score(1L)
                .build();
    }
    public void setCount(Long score){
        this.score=score;
    }

}
