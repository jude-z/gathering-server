package repository.querydsl.meeting;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QueryDslMeetingRepository {
    private final JPAQueryFactory queryFactory;


}
