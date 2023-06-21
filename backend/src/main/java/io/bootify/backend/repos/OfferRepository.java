package io.bootify.backend.repos;

import io.bootify.backend.domain.Offer;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OfferRepository extends JpaRepository<Offer, Long> {

        @EntityGraph(attributePaths = "offerImages")
        Optional<Offer> findById(Long id);
}
