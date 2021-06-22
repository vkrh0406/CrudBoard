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

    public void checkSearchType(){
        if(this.getSearchType().equals("title")){
            this.setTitle(this.getKeyword());
        }
        else if(this.getSearchType().equals("writer")){
            this.setWriter(this.getKeyword());
        }
        else if(this.getSearchType().equals("content")){
            this.setContent(this.getKeyword());
        }
        else if(this.getSearchType().equals("titleAndContent")){
            this.setTitle(this.getKeyword());
            this.setContent(this.getKeyword());
        } else if (this.getSearchType().equals("all")) {
            this.setTitle(this.getKeyword());
            this.setContent(this.getKeyword());
            this.setWriter(this.getKeyword());
        }
    }

}
