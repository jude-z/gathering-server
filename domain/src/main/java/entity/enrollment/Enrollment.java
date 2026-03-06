package entity.enrollment;

import entity.gathering.Gathering;
import entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "enrollment")
@AllArgsConstructor
@Builder
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean accepted;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_id")
    private Gathering gathering;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User enrolledBy;
    private LocalDateTime date;

    public void changeAccepted(){
        this.accepted = true;
    }

    public static Enrollment of(boolean accepted, Gathering gathering, User enrolledBy,LocalDateTime date) {
        return Enrollment.builder()
                .accepted(accepted)
                .gathering(gathering)
                .enrolledBy(enrolledBy)
                .date(date)
                .build();
    }


}
