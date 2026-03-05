package entity.fcm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.myproject.entity.user.User;

import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "fcm_token")
public class FCMToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "token_value", unique = true)
    private String tokenValue;
    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void changeExpirationDate(int month){
        expirationDate = LocalDate.now().plusMonths(month);
    }
}
