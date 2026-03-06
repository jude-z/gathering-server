package repository.querydsl.board;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dto.PageableInfo;
import dto.querydsl.QueryDslPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import response.board.querydto.BoardQuery;
import response.board.querydto.BoardsQuery;

import java.util.List;

@RequiredArgsConstructor
public class QueryDslBoardRepository {
    private final JPAQueryFactory queryFactory;

    QueryDslPageResponse<BoardQuery> fetchBoard(PageableInfo pageableInfo, Long boardId){

    }

    Page<BoardsQuery> fetchBoards(PageableInfo pageableInfo){

    }

}
