package repository.querydsl.chat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QueryDslChatRepository {
    private final JPAQueryFactory queryFactory;


}
