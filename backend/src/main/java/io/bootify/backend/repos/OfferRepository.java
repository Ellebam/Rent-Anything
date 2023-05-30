package io.bootify.backend.repos;

import io.bootify.backend.domain.Offer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OfferRepository extends JpaRepository<Offer, Long> {
}
