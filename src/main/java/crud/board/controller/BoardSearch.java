package crud.board.controller;

import crud.board.domain.SearchType;
import lombok.Data;

import javax.naming.directory.SearchControls;

@Data
public class BoardSearch {


    private String searchType;
    private String keyword;
    private String title;
    private String content;
    private String writer;
    private String titleAndContent;

}
