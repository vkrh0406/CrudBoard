package crud.board.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;


    private String loginId;
    private String password;

    private String username;

    private int age;

    @OneToMany(mappedBy = "member")
    private List<Board> boards = new ArrayList<>();

    @Embedded
    private Address address;


    public void addBoard(Board board) {
        this.boards.add(board);
    }

    public Member() {
    }


    public Member(String loginId, String password, String username, int age, Address address) {
        this.loginId = loginId;
        this.password = password;
        this.username = username;
        this.age = age;
        this.address = address;
    }

    public void updateMember(String password, String username, int age, Address address){
        this.password = password;
        this.username = username;
        this.age = age;
        this.address = address;

    }

}
