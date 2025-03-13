package com.emotionalcart.member.infrasturcture;

import com.emotionalcart.core.feature.Member;
import com.emotionalcart.core.feature.enums.MemberRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findBySocialId(String socialId);

    Optional<Member> findByEmail(String email);

    Page<Member> findAllByMemberRoles(MemberRole memberRole, Pageable pageable);

    Optional<Member> findByIdAndMemberRoles(Long id, MemberRole memberRole);
}
