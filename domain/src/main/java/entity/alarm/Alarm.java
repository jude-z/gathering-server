package entity.alarm;


import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Builder
@Table(name = "alarm")
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private LocalDateTime date;
    private Boolean checked;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Alarm from(String content,User user){
        return Alarm.builder()
                .content(content)
                .user(user)
                .date(LocalDateTime.now())
                .checked(false)
                .build();
    }

}
