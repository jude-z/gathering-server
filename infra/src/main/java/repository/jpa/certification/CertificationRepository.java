package repository.jpa.certification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.entity.certification.Certification;

public interface CertificationRepository extends JpaRepository<Certification, Long> {
    @Query("select c.certification from Certification c where c.email = :email order by c.id desc limit 1")
    String findCertificationByEmail(String email);
}
