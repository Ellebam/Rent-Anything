package io.bootify.backend.repos;

import io.bootify.backend.domain.RentalApplication;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RentalApplicationRepository extends JpaRepository<RentalApplication, Long> {
}
