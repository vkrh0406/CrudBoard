package crud.board.repository;

import crud.board.domain.Board;
import crud.board.domain.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    public Optional<Comment> findCommentById(Long id);

    public void deleteCommentsByBoard(Board board);



}
