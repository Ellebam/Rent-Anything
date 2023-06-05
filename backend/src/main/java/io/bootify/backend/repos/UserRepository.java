package io.bootify.backend.repos;

import io.bootify.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    
    boolean existsByUsernameIgnoreCase(String username);

}
