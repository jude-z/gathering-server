package repository.querydsl.board;

import entity.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import response.board.querydto.BoardQuery;
import response.board.querydto.BoardsQuery;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("select new spring.myproject.dto.response.board.querydto.BoardQuery" +
            "(b.title,b.description,i.url,u.username,u.nickname,im.url,b.registerDate) " +
            "from Board b " +
            "left join b.user u left join u.profileImage im " +
            "left join Image i on i.board.id = b.id where b.id = :boardId")
    List<BoardQuery> fetchBoard(Long boardId);

    @Query("select new spring.myproject.dto.response.board.querydto." +
            "BoardsQuery(b.id,b.title,b.description,u.nickname,b.registerDate,i.url) " +
            "from Board b " +
            "join b.user u left join u.profileImage i")
    Page<BoardsQuery> fetchBoards(Pageable pageable);
}
