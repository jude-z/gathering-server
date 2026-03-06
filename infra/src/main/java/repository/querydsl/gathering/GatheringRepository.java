package repository.querydsl.gathering;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.dto.response.gathering.querydto.GatheringDetailQuery;
import spring.myproject.dto.response.gathering.querydto.GatheringsQuery;
import spring.myproject.dto.response.gathering.querydto.MainGatheringsQuery;
import spring.myproject.dto.response.gathering.querydto.ParticipatedQuery;
import spring.myproject.entity.gathering.Gathering;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface GatheringRepository extends JpaRepository<Gathering,Long> {
    @Query(
//            "select " +
//            "new spring.myproject.dto.response.gathering.querydto.GatheringDetailQuery" +
//            "(g.id,g.title,g.content,g.registerDate,ca.name,cr.username,crm.url,u.username,u.nickname,pm.url,im.url,g.count) " +
//            "from Gathering g " +
//            "join g.createBy cr " +
//            "join cr.profileImage crm " +
//            "join Category ca on ca.gathering.id = g.id " +
//            "join g.gatheringImage im " +
//            "left join g.enrollments e " +
//            "left join e.enrolledBy u " +
//            "left join u.profileImage pm " +
//            "where g.id = :gatheringId order by u.id asc limit 9"
            value = "select " +
                    "g.id as id, g.title as title, g.content as content, g.register_date as registerDate, " +
                    "ca.name as category, cr.username as createdBy, crm.url as createdByUrl, " +
                    "u.username as participatedBy, u.nickname as participatedByNickname, " +
                    "pm.url as participatedByUrl, im.url as url, g.count as count " +
                    "from gathering g " +
                    "join category ca on ca.id = g.category_id " +
                    "join user cr on g.user_id = cr.id " +
                    "join image crm on cr.image_id = crm.id " +
                    "join image im on g.image_id = im.id " +
                    "left join " +
                    "(select e.* " +
                    "from enrollment e " +
                    "left join user u on u.id = e.user_id " +
                    "left join gathering ge on ge.id = e.gathering_id and ge.id = :gatheringId " +
                    "order by u.id " +
                    "limit 10) " +
                    "e on e.gathering_id = g.id " +
                    "left join user u on u.id = e.user_id " +
                    "left join image pm on u.image_id = pm.id " +
                    "where g.id = :gatheringId " +
                    "order by u.id",
            nativeQuery = true
    )
    List<GatheringDetailQuery> gatheringDetail(Long gatheringId);
    @Query("select new spring.myproject.dto.response.gathering.querydto.ParticipatedQuery(u.username,u.nickname,i.url) " +
            "from Gathering g " +
            "left join g.enrollments e " +
            "left join e.enrolledBy u " +
            "left join u.profileImage i where g.id =:gatheringId")
    Page<ParticipatedQuery> gatheringParticipated(Long gatheringId, Pageable pageable);
    @Query(value = "select id,title,content,registerDate,category,createdBy,url,count from ( " +
            "  select g.id as id, g.title as title, g.content as content, g.register_date as registerDate, ca.name as category, " +
            "         cr.username as createdBy, im.url as url, g.count as count, " +
            "         row_number() over (partition by ca.name order by g.count desc) as rownum " +
            "  from gathering g " +
            "  left join category ca on g.category_id = ca.id " +
            "  left join user cr on g.user_id = cr.id " +
            "  left join image im on g.image_id = im.id " +
            ") as subquery " +
            "where rownum between 1 and 9", nativeQuery = true)
    List<MainGatheringsQuery> gatherings();

    @Query(
            value =
//                    "SELECT " +
//                    "g.id as id, g.title as title, g.content as content, g.register_date as registerDate, " +
//                    "ca.name as category, cr.username as createdBy, im.url as url, g.count as count " +
//                    "FROM ( " +
//                    "SELECT gathering_id, name " +
//                    "FROM category " +
//                    "WHERE name = :name " +
//                    ") ca " +
//                    "LEFT JOIN gathering g ON ca.gathering_id = g.id " +
//                    "LEFT JOIN user cr ON g.user_id = cr.id " +
//                    "LEFT JOIN image im ON g.image_id = im.id " +
//                    "ORDER BY g.count DESC " +
//                    "LIMIT 9"
                    "SELECT " +
                            "g.id as id, g.title as title, g.content as content, g.register_date as registerDate, " +
                            "g.category_name AS category, " +
                            "cr.username AS createdBy, " +
                            "im.url as url, " +
                            "g.count as count " +
                            "FROM ( " +
                            "SELECT " +
                            "g.*, " +
                            "ca.name AS category_name " +
                            "FROM gathering g " +
                            "JOIN category ca ON ca.id = g.category_id " +
                            "WHERE ca.name = :name " +
                            "ORDER BY g.count DESC " +
                            "LIMIT 9 " +
                            ") g " +
                            "LEFT JOIN user cr ON g.user_id = cr.id " +
                            "LEFT JOIN image im ON g.image_id = im.id"
    ,nativeQuery = true
    )
    List<MainGatheringsQuery> subGatherings(String name);

    @Query("select new spring.myproject.dto.response.gathering.querydto." +
            "GatheringsQuery(g.id,g.title,g.content,g.registerDate,ca.name,cr.username,im.url,g.count) " +
            "from Gathering g " +
            "left join Category ca on ca.id = g.category.id " +
            "left join  g.createBy cr " +
            "left join  g.gatheringImage im " +
            "where ca.name =:category"
    )
    Page<GatheringsQuery> gatheringsCategory(PageRequest pageRequest, String category);

    @Query("select new spring.myproject.dto.response.gathering.querydto." +
            "GatheringsQuery(g.id,g.title,g.content,g.registerDate,ca.name,cr.username,im.url,g.count) " +
            "from Gathering g left join g.gatheringImage im " +
            "left join Category ca on ca.id = g.category.id " +
            "left join g.createBy cr " +
            "left join Like l on l.gathering.id = g.id " +
            "left join l.likedBy u " +
            "where u.id=:userId")
    Page<GatheringsQuery> gatheringsLike(Pageable pageable, Long userId);

    @Query("select new spring.myproject.dto.response.gathering.querydto." +
            "GatheringsQuery(g.id,g.title,g.content,g.registerDate,ca.name,cr.username,im.url,g.count) " +
            "from Recommend r " +
            "join r.gathering g " +
            "join Category ca on ca.id = g.category.id " +
            "join g.createBy cr " +
            "left join g.gatheringImage im " +
            "where r.date = :localDate order by r.score desc limit 10")
    List<GatheringsQuery> gatheringsRecommend(LocalDate localDate);

    @Query("select g from Gathering g left join fetch g.topic t where g.id = :gatheringId")
    Optional<Gathering> findTopicById(Long gatheringId);

    @Query("select g from Gathering g " +
            "left join fetch g.createBy u " +
            "left join fetch u.tokens t " +
            "where g.id = :gatheringId")
    Optional<Gathering> findGatheringFetchCreatedByAndTokensId(Long gatheringId);
    @Query("select g from Gathering g " +
            "left join fetch g.createBy u " +
            "left join fetch g.topic t " +
            "where g.id = :gatheringId")
    Optional<Gathering> findGatheringFetchCreatedAndTopicBy(Long gatheringId);

    @Query(value = "update gathering set count = count + :val where id = :gatheringId"
            ,nativeQuery = true)
    @Modifying
    void updateCount(Long gatheringId, int val);

}
