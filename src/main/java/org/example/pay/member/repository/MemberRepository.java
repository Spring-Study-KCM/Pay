package org.example.pay.member.repository;

import java.util.Optional;

import org.example.pay.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByEmail(String email);
	boolean existsByEmail(String email);
}
