package crud.board.repository;


import crud.board.domain.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardRepository {



    private final EntityManager em;

    @Transactional
    public Long save(Board board){
        em.persist(board);

        return board.getId();
    }

    public Board findOne(Long id){
        return em.find(Board.class, id);
    }

    public List<Board> findAll(){
        return em.createQuery("select b from Board b", Board.class)
                .getResultList();
    }

    @Transactional
    public void delete(Board board){
        em.remove(board);
    }



}
