package repository.jpa.meeting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.dto.response.meeting.querydto.MeetingDetailQuery;
import spring.myproject.dto.response.meeting.querydto.MeetingsQueryInterface;
import spring.myproject.entity.meeting.Meeting;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting,Long> {

    @Query("select new spring.myproject.dto.response.meeting.querydto." +
            "MeetingDetailQuery(m.id,m.title,cr.username,cr.nickname,cri.url,u.username,u.nickname,ui.url,m.meetingDate,m.endDate,m.content,m.count,i.url) " +
            "from Meeting m " +
            "left join m.attends a " +
            "left join a.attendBy u " +
            "left join u.profileImage ui " +
            "left join m.createdBy cr " +
            "left join cr.profileImage cri " +
            "left join m.image i on i.id=m.image.id " +
            "where m.id = :meetingId")
    List<MeetingDetailQuery> meetingDetail(Long meetingId);

    @Query(value = """
    select m.id as id, m.title as title, cr.username as createdBy, cr.nickname as createdbyNickname, m.meeting_date as meetingDate, m.end_date as endDate,
           m.content as content, m.count as count, i.url as url, au.id as participatedId, pi.url as participatedImageUrl
    from (
        select m2.id
        from meeting m2
        where m2.gathering_id = :gatheringId
        order by m2.id asc
        limit 9 offset :offset
    ) as sub
    join meeting m on m.id = sub.id
    left join user cr on m.user_id = cr.id
    left join image i on m.image_id = i.id
    left join gathering g on m.gathering_id = g.id
    left join attend a on a.meeting_id = m.id
    left join user au on a.user_id = au.id
    left join image pi on au.image_id = pi.id
    where g.id = :gatheringId
    """, nativeQuery = true)
    List<MeetingsQueryInterface> meetings(Integer offset,Long gatheringId);

    @Query(value = "update meeting set count = count + :val where id = :meetingId",
    nativeQuery = true)
    @Modifying
    void updateCount(Long meetingId, int val);
}
