package io.bootify.backend.repos;

import io.bootify.backend.domain.Message;
import io.bootify.backend.domain.Offer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MessageRepository extends JpaRepository<Message, Long> {
        List<Message> findByOffer(Offer offer);
}
