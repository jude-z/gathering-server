package repository.querydsl.gathering;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QueryDslGatheringRepository {
    private final JPAQueryFactory queryFactory;


}
