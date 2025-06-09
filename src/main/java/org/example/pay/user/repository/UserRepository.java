package org.example.pay.user.repository;

import java.util.Optional;
import org.example.pay.user.domain.User;
import org.example.pay.user.repository.custom.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    Optional<User> findByEmail(String username);

    boolean existsByEmail(String email);
}
