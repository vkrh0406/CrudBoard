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

    public void delete(Long boardId,String password) throws IllegalAccessException {

        Board findOne = boardRepository.findOne(boardId);
        if (!findOne.getPassword().equals(password)) {

            throw new IllegalAccessException("패스워드가 다릅니다");
        }
        else {
            boardRepository.delete(findOne);
        }


    }

    public Page<BoardDto> search(BoardSearch boardSearch, Pageable pageable) throws IllegalArgumentException {

        if (pageable.getPageSize() >= 100) {
            throw new IllegalArgumentException("PageSize so Big");
        }

        // 검색조건타입이 없으면 전부 조회
        if (boardSearch.getSearchType()==null || boardSearch.getSearchType().equals("")) {
            Page<BoardDto> boardList = boardRepository.findAll(pageable);

            return boardList;

        }// 검색조건타입에 따라 객체에 키워드를 집어넣음
        else {
            boardSearch.checkSearchType();
        }



        Page<BoardDto> boardDto = boardRepository.searchBoard(boardSearch, pageable);

        return boardDto;

    }



}
