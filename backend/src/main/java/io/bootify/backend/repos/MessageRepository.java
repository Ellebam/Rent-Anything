package io.bootify.backend.repos;

import io.bootify.backend.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MessageRepository extends JpaRepository<Message, Long> {
}
