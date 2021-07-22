package crud.board;

import crud.board.domain.Board;
import crud.board.domain.Member;
import crud.board.dto.BoardDto;
import crud.board.repository.BoardRepository;
import crud.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;
    @PostConstruct
    public void init(){
        initService.initDb();
    }


    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final BoardRepository boardRepository;
        private final MemberRepository memberRepository;



        public void initDb(){


            if (boardRepository.findAll(PageRequest.of(0,10)).getContent().size() != 0) {
                return;
            }

            Page<BoardDto> all = boardRepository.findAll(PageRequest.of(0,10));
            if (all.getContent().size() > 0) {
                return;
            }
            for (int i = 0; i < 150; i++) {

                Board board = new Board("제목" + i, "작성자" + i, "내용" + i, "1234");
                boardRepository.save(board);

            }

            Member member = new Member();
            member.setLoginId("test");
            member.setPassword("test!");
            member.setAge(12);
            member.setUsername("익명아님");
            memberRepository.save(member);

        }



    }

}
