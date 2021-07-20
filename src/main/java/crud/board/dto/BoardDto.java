package crud.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import crud.board.domain.Member;
import crud.board.domain.UploadFile;
import lombok.Data;
import lombok.Getter;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BoardDto {

    private Long id;
    private String title;
    private String writer;
    private String content;
    private int views;
    private LocalDateTime updatedTime;
    private List<UploadFile> uploadFiles;

    public void setUploadFiles(List<UploadFile> uploadFiles) {
        this.uploadFiles = uploadFiles;
    }



    @QueryProjection
    public BoardDto(Long id, String title, String writer, String content, int views, LocalDateTime updatedTime) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.views = views;
        this.updatedTime = updatedTime;
    }
}
