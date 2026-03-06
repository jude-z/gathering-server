package repository.jpa.image;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.entity.image.Image;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image,Long> {
    @Query("select i.url from Image i join i.gathering g")
    Page<String> gatheringImage(Long gatheringId, Pageable pageable);

    Optional<Image> findByUrl(String imageUrl);
}
