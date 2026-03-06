package repository.jpa.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUsername(String username);

    Boolean existsByNickname(String nickname);

    @Query("select u from User u where u.email = :email")
    List<User> findByEmail(String email);

    @Query("select u from User u " +
            "left join fetch u.profileImage where u.id = :userId")
    Optional<User> findByIdFetchImage(Long userId);

    @Query("select u from User u left join fetch FCMToken t on t.user.id = u.id where u.id =:userId")
    Optional<User> findAndTokenByUserId(Long userId);

    @Query("select u from User u where u.username =:username")
    Optional<User> findByUsername(String username);

    @Query("select u from Gathering g " +
            "join g.enrollments e " +
            "join e.enrolledBy u " +
            "where g.id = :gatheringId and u.id != :userId")
    List<User> findEnrollmentById(Long gatheringId, Long userId);
}
