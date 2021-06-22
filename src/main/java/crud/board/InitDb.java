package crud.board;

import crud.board.domain.Board;
import crud.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;
    @PostConstruct
    public void init(){
        //initService.initDb();
    }


    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final BoardRepository boardRepository;



        public void initDb(){

            for (int i = 0; i < 150; i++) {

                Board board = new Board("제목" + i, "작성자" + i, "내용" + i, "1234");
                boardRepository.save(board);


            }
        }



    }

}
