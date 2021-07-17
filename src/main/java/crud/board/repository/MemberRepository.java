package crud.board.repository;

import crud.board.domain.Member;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    public Optional<Member> findMemberByLoginId(String loginId);

    public Optional<Member> findMemberById(Long id);



}
