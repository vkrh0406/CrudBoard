package crud.board.controller.form;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class CommentForm {

    @NotEmpty
    private String writer;
    @NotEmpty
    private String password;
    @NotEmpty
    private String content;





}
