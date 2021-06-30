package crud.board.repository;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import crud.board.controller.BoardSearch;
import crud.board.domain.Board;
import crud.board.dto.BoardDto;
import crud.board.dto.QBoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
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

    public Page<BoardDto> findAll(Pageable pageable) {
        QueryResults<BoardDto> boardDtoQueryResults = queryFactory
                .select(new QBoardDto(board.id, board.title, board.writer, board.content, board.updatedTime))
                .from(board)
                .orderBy(board.id.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchResults();

        List<BoardDto> content = boardDtoQueryResults.getResults();

        long total = boardDtoQueryResults.getTotal();

        return new PageImpl<>(content, pageable, total);


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
                .where(searchCondition(boardSearch.getTitle(), boardSearch.getContent(), boardSearch.getWriter()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(board.id.desc())
                .fetch();

        JPAQuery<Board> countQuery = queryFactory
                .select(board)
                .from(board)
                .where(searchCondition(boardSearch.getTitle(), boardSearch.getContent(), boardSearch.getWriter()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

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
     * booleanBuilder 를 활용해  Or 조건을 집어넣었습니다.
     *
     * @param title
     * @param content
     * @return
     */
    private BooleanBuilder searchCondition(String title, String content, String writer) {



        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (hasText(title)) {
            booleanBuilder.or(board.title.contains(title));
        }
        if (hasText(content)) {
            booleanBuilder.or(board.content.contains(content));
        }
        if (hasText(writer)) {
            booleanBuilder.or(board.writer.contains(writer));
        }


        return booleanBuilder;

    }


}
