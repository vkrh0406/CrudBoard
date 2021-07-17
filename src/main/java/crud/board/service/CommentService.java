package crud.board.service;

import crud.board.controller.form.CommentForm;
import crud.board.domain.Board;
import crud.board.domain.Comment;
import crud.board.domain.Member;
import crud.board.repository.BoardRepository;
import crud.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardService boardService;
    private final MemberService memberService;



    public void deleteComment(Long commentId, Member member, String password) {

        Comment comment = commentRepository.findCommentById(commentId).orElse(null);

        if (comment == null) {
            return;
        }
        if (member != null) {
            if (comment.getMember().getId().equals(member.getId())) {
                commentRepository.delete(comment);
                return;
            }
        }

        if (comment.getPassword().equals(password)) {
            commentRepository.delete(comment);
        }


    }


    public Optional<Comment> findCommentById(Long id) {
        return commentRepository.findCommentById(id);
    }

    public List<Comment> findAllComment() {
        return commentRepository.findAll();
    }

    public void saveComment(Long boardId, CommentForm commentForm) {

        Board findOne = boardService.findOne(boardId);

        Comment comment = new Comment(commentForm.getWriter(), commentForm.getContent(), commentForm.getPassword());


        // 게시판에 댓글 추가
        findOne.addComment(comment);

        if (comment.getWriter() != null && comment.getContent() != null) {
            commentRepository.save(comment);
        }

    }

    public void saveComment(Long boardId, Member member, CommentForm commentForm) {

        Board findBoard = boardService.findOne(boardId);
        Member findMember = memberService.findMemberById(member.getId());

        Comment comment = new Comment(commentForm.getWriter(), commentForm.getContent(),commentForm.getPassword());


        // 게시판에 댓글 추가
        findBoard.addComment(comment);

        //멤버에 댓글 추가
        findMember.addComment(comment);



        if (comment.getWriter() != null && comment.getContent() != null) {
            commentRepository.save(comment);
        }

    }

}
