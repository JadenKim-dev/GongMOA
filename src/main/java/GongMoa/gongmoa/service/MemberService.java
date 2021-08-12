package GongMoa.gongmoa.service;

import GongMoa.gongmoa.domain.Member;
import GongMoa.gongmoa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long createMember(Member member) {
        return memberRepository.save(member).getId();
    }

    @Transactional
    public void deleteMember(Member member) {
        memberRepository.delete(member);
        return;
    }

    public Optional<Member> findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
