package crud.board.repository;

import crud.board.controller.BoardSearch;
import crud.board.domain.Board;
import crud.board.dto.BoardDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BoardRepositoryTest {


    @Autowired
    BoardRepository boardRepository;



    @Test
    void saveTest() {
        //given
        Board board = new Board("test1", "test", "test", "1234");

        //when
        boardRepository.save(board);
        Board findOne = boardRepository.findOne(board.getId());

        //then
        Assertions.assertThat(board).isEqualTo(findOne);
    }

    @Test
    void deleteTest() {
        //given
        Board board = new Board("test1", "test", "test", "1234");
        Long savedId = boardRepository.save(board);

        //when
        boardRepository.delete(board);
        Board findOne = boardRepository.findOne(savedId);

        //then
        Assertions.assertThat(findOne).isNull();

    }

    @Test
    void searchTest() {
        //given
        Board board = new Board("test1", "test", "test", "1234");
        Long savedId = boardRepository.save(board);

        Pageable pageable = PageRequest.of(0, 10);
        BoardSearch boardSearch = new BoardSearch();
        boardSearch.setContent("test");


        //when
        Page<BoardDto> boardDtos = boardRepository.searchBoard(boardSearch, pageable);


        //then
        Assertions.assertThat(boardDtos.getContent().get(0).getContent()).isEqualTo(boardSearch.getContent());

    }
}