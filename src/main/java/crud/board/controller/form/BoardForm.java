package crud.board.controller.form;


import crud.board.domain.UploadFile;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BoardForm {
    @NotEmpty(message="제목 이름은 필수 입니다")
    private String title;
    @NotEmpty(message = "작성자 이름은 필수 입니다")
    private String writer;
    @NotEmpty(message = "내용은 필수 입니다")
    private String content;
    @NotEmpty(message = "패스워드는 필수 입니다")
    private String password;

    private List<MultipartFile> multipartFiles;

    private Long id;



}
