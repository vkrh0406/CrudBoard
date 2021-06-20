package crud.board.controller.form;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class BoardForm {
    @NotEmpty(message="제목 이름은 필수 입니다")
    private String title;
    @NotEmpty(message = "작성자 이름은 필수 입니다")
    private String writer;
    @NotEmpty(message = "내용은 필수 입니다")
    private String content;



}
