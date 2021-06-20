package crud.board.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;

    private int age;

    @OneToMany(mappedBy = "member")
    private List<Board> boards = new ArrayList<>();

    @Embedded
    private Address address;

}
