package repository.querydsl.recommend;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QueryDslRecommendRepository {
    private final JPAQueryFactory queryFactory;


}
