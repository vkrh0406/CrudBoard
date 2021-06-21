package crud.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import crud.board.domain.Member;
import lombok.Data;
import lombok.Getter;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
public class BoardDto {

    private Long id;
    private String title;
    private String writer;
    private String content;
    private LocalDateTime updatedTime;

    @QueryProjection
    public BoardDto(Long id, String title, String writer, String content,LocalDateTime updatedTime) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.updatedTime = updatedTime;
    }
}
