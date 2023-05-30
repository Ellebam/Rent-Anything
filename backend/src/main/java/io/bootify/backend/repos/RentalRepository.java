package io.bootify.backend.repos;

import io.bootify.backend.domain.Rental;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RentalRepository extends JpaRepository<Rental, Long> {
}
