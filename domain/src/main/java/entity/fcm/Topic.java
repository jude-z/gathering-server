package entity.fcm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.myproject.entity.gathering.Gathering;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "topic_name")
    private String topicName;
    @OneToOne(mappedBy = "topic", optional = false)
    private Gathering gathering;

}

