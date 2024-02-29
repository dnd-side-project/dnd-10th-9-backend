package com.dnd.dotchi.domain.member.repository;

import com.dnd.dotchi.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
