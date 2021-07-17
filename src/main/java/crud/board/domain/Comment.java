package crud.board.domain;

import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    private String writer;

    private String content;

    private String password;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "board_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;


    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;


    protected Comment() {
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Comment(String writer, String content, String password) {
        this.writer = writer;
        this.content = content;
        this.password = password;
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
    }

    public void updateComment(String content) {
        this.content = content;
        this.updatedTime = LocalDateTime.now();
    }
}
