package crud.board.repository;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import crud.board.controller.BoardSearch;
import crud.board.domain.Board;
import crud.board.dto.BoardDto;
import crud.board.dto.QBoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.swing.text.StyledEditorKit;
import java.util.List;

import static crud.board.domain.QBoard.board;
import static org.springframework.util.StringUtils.hasText;

@Repository
@Transactional(readOnly = true)
public class BoardRepository {


    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public BoardRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Transactional
    public Long save(Board board) {
        em.persist(board);

        return board.getId();
    }

    public Board findOne(Long id) {
        return em.find(Board.class, id);
    }

    public List<Board> findAll() {
        return em.createQuery("select b from Board b", Board.class)
                .getResultList();
    }

    @Transactional
    public void delete(Board board) {
        em.remove(board);
    }

    @Transactional
    public Page<BoardDto> searchBoard(BoardSearch boardSearch, Pageable pageable) {

        List<BoardDto> content = queryFactory
                .select(new QBoardDto(board.id, board.title, board.writer, board.content, board.updatedTime))
                .from(board)
                .where(titleAndContentContain(boardSearch.getTitle(), boardSearch.getContent()),
                        writerContain(boardSearch.getWriter()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Board> countQuery = queryFactory
                .select(board)
                .from(board)
                .where(titleAndContentContain(boardSearch.getTitle(), boardSearch.getContent()),
                        writerContain(boardSearch.getWriter()) );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);


    }

    private BooleanExpression titleContain(String title) {
        return (hasText(title)) ? board.title.contains(title) : null;
    }

    private BooleanExpression writerContain(String writer) {
        return (hasText(writer)) ? board.writer.contains(writer) : null;
    }

    private BooleanExpression contentContain(String content) {
        return (hasText(content)) ? board.content.contains(content) : null;
    }

    /**
     * 제목과 내용 둘중 하나가 널일 가능성이 있지만 Or에는 Null이 들어가면 안된다.. 그래서 메소드로 만듦
     * @param title
     * @param content
     * @return
     */
    private BooleanExpression titleAndContentContain(String title,String content) {

        BooleanExpression contains;
        if (hasText(title) && hasText(content)) {
            contains = board.title.contains(title).or(board.content.contains(content));
        }
        else if(hasText(title)){
            contains = board.title.contains(title);
        }
        else{
            contains = board.title.contains(content);
        }

        return contains;

    }


}
