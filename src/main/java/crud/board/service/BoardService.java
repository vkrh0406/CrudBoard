package crud.board.service;


import crud.board.controller.BoardSearch;
import crud.board.domain.Board;
import crud.board.dto.BoardDto;
import crud.board.repository.BoardRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {


    private final BoardRepository boardRepository;

    //글 생성
    public Long CreateBoard(Board board) {
        return boardRepository.save(board);
    }

    //게시판 전체조회
    public Page<BoardDto> findBoardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    //글 하나 찾기
    public Board findOne(Long boardId) {
        return boardRepository.findOne(boardId);
    }

    //글 제목, 내용 업데이트
    public void update(Long id, String title, String content) {
        Board findOne = boardRepository.findOne(id);
        findOne.update(title, content);

    }

    public void delete(Board board) {
        boardRepository.delete(board);
    }

    public Page<BoardDto> search(BoardSearch boardSearch, Pageable pageable) {

//        //게시판 정렬
//        int page;
//        if (pageable.getPageNumber() <= 0) {
//            page = 0;
//        } else {
//            page = pageable.getPageNumber() - 1;
//        }
//        Pageable requestPageable = PageRequest.of(page, pageable.getPageSize());


        Page<BoardDto> boardDto = boardRepository.searchBoard(boardSearch, pageable);

        return boardDto;

    }



}
