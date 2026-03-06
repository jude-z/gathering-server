package repository.jpa.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.entity.like.Like;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like,Long> {

    @Query("select l from Like l " +
            "left join l.gathering g left join l.likedBy u " +
            "where g.id = :gatheringId and u.id = :userId")
    Optional<Like> findLike(Long userId, Long gatheringId);



}
