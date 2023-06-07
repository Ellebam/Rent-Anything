// OfferImageRepository.java

package io.bootify.backend.repos;

import io.bootify.backend.domain.OfferImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferImageRepository extends JpaRepository<OfferImage, Long> {
}
