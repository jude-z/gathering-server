package infra.repository.certification;

import com.querydsl.jpa.impl.JPAQueryFactory;
import entity.certification.Certification;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static infra.utils.DummyData.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.yml")
class QueryDslCertificationRepositoryTest {

    @Autowired EntityManager em;
    @Autowired CertificationRepository certificationRepository;

    QueryDslCertificationRepository queryDslCertificationRepository;

    @BeforeEach
    void setUp() {
        queryDslCertificationRepository = new QueryDslCertificationRepository(new JPAQueryFactory(em));
    }

    @Test
    void findCertificationByEmail() {
        Certification cert1 = returnDummyCertification("test@test.com", "111111");
        Certification cert2 = returnDummyCertification("test@test.com", "222222");
        certificationRepository.saveAll(java.util.List.of(cert1, cert2));

        String result = queryDslCertificationRepository.findCertificationByEmail("test@test.com");

        assertThat(result).isEqualTo("222222");
    }
}
