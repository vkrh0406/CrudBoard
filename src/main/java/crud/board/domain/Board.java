package crud.board.domain;

import lombok.Getter;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Board {

    @Id
    @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    private String title;
    private String writer;

    private String content;

    private String password;

    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    protected Board() {

    }

    public Board(String title, String writer, String content,String password) {
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.password=password;
        this.createdTime = LocalDateTime.now();
        this.updatedTime = this.createdTime;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
        this.updatedTime = LocalDateTime.now();
    }

}
