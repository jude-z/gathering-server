package entity.certification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static spring.myproject.dto.request.user.UserRequestDto.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "certification")
public class Certification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String certification;

    public static Certification of(String email, String certification) {
        return Certification.builder()
                .email(email)
                .certification(certification)
                .build();
    }
}
