package crud.board.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.jni.Local;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private int views;


    @ElementCollection
    private List<UploadFile> uploadFiles;



    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "board",cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();


    public void setViews(int views) {
        this.views = views;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void addViews(){
        views+=1;
    }
    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setBoard(this);
    }

    protected Board() {

    }

    public void setUploadFiles(List<UploadFile> uploadFiles) {
        this.uploadFiles = uploadFiles;
    }

    public Board(String title, String writer, String content, String password) {
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
