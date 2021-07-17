package crud.board.service;

import crud.board.domain.Member;
import crud.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;


    public Member findMemberById(Long id) {
        Optional<Member> memberById = memberRepository.findMemberById(id);

        return memberById.orElse(null);
    }

    public Long saveMember(Member member) {

        Optional<Member> memberByLoginId = memberRepository.findMemberByLoginId(member.getLoginId());
        Member findMember = memberByLoginId.orElse(null);

        //로그인 아이디 중복이면 null 리턴
        if (findMember != null) {
            return null;
        }

        memberRepository.save(member);


        return member.getId();

    }


    public List<Member> findAllMember() {
        return memberRepository.findAll();
    }

    public void deleteMember(Long id) {
        Optional<Member> member = memberRepository.findById(id);
        Member findMember = member.orElseThrow(() -> new NoSuchElementException());

        memberRepository.delete(findMember);

    }

    public void updateMember(Member member) {
        Optional<Member> findOne = memberRepository.findById(member.getId());
        Member findMember = findOne.orElseThrow();

        findMember.updateMember(member.getPassword(), member.getUsername(), member.getAge(), member.getAddress());


    }

    public Member login(String loginId, String password) {

        Optional<Member> memberByLoginId = memberRepository.findMemberByLoginId(loginId);
        return memberByLoginId.filter(o -> o.getPassword().equals(password))
                .orElse(null);

    }


}
