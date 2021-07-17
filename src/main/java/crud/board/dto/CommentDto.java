package crud.board.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    private String writer;
    private String content;

    private LocalDateTime updatedTime;


    public CommentDto(Long id,String writer, String content, LocalDateTime updatedTime) {
        this.id = id;
        this.writer = writer;
        this.content = content;
        this.updatedTime = updatedTime;
    }
}
