package repository.jpa.enrollment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.entity.enrollment.Enrollment;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.entity.user.User;

import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {

    @Query("select e from Enrollment e where e.gathering.id = :gatheringId and e.enrolledBy.id = :userId")
    Enrollment existEnrollment(Long gatheringId,Long userId);


    @Query("select e from Enrollment e " +
            "join fetch e.enrolledBy u " +
            "left join fetch u.tokens t " +
            "where e.gathering.id = :gatheringId and u.id = :userId and e.accepted = :accepted")
    Optional<Enrollment> findEnrollment(Long gatheringId, Long userId,boolean accepted);

    @Query("select e from Enrollment e " +
            "left join fetch e.enrolledBy u " +
            "left join fetch u.tokens t " +
            "where e.id = :enrollmentId and e.accepted = false")
    Optional<Enrollment> findEnrollmentEnrolledByAndTokensById(Long enrollmentId);


    Optional<Enrollment> findByGatheringAndEnrolledBy(Gathering gathering, User enrolledBy);
}
