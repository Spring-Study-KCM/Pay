package org.example.pay.member.repository;

import java.util.Optional;

import org.example.pay.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByEmail(String email);
	boolean existsByEmail(String email);

	@Query("select m from Member m join fetch m.wallet where m.id = :id")
	Optional<Member> findByIdWithWallet(@Param("id") Long id);

	@Query("select m from Member m "
		+ "join fetch m.wallet "
		+ "join fetch m.accounts "
		+ "where m.id = :id")
	Optional<Member> findByIdWithWalletAndAccounts(@Param("id") Long id);

}
