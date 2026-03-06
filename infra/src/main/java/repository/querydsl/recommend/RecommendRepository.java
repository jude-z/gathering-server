package repository.querydsl.recommend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.entity.recommend.Recommend;

import java.time.LocalDate;

public interface RecommendRepository extends JpaRepository<Recommend,Long> {

    @Query(value =
            "insert into recommend(gathering_id,score,date) " +
                    "values (:gatheringId,1,:localDate) " +
                    "on duplicate key update score = score + :val"
            ,nativeQuery = true)
    @Modifying
    int updateCount(Long gatheringId,LocalDate localDate,int val);

}
