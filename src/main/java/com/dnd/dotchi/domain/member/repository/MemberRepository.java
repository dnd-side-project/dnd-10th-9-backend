package com.dnd.dotchi.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dnd.dotchi.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
