package io.bootify.backend.repos;

import io.bootify.backend.domain.RentalApplication;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface RentalApplicationRepository extends JpaRepository<RentalApplication, Long> {
    @Query("SELECT ra FROM RentalApplication ra JOIN FETCH ra.offer o JOIN FETCH o.user WHERE ra.id = :id")
    Optional<RentalApplication> findByIdWithOfferAndUser(@Param("id") Long id);


}
