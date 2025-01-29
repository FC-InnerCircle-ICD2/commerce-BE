package com.emotionalcart.auth.infrasturcture;

import com.emotionalcart.core.feature.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findBySocialId(String socialId);
}
