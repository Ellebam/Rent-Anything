package io.bootify.backend.service;

import io.bootify.backend.domain.Offer;
import io.bootify.backend.domain.Rental;
import io.bootify.backend.model.RentalDTO;
import io.bootify.backend.repos.RentalRepository;
import io.bootify.backend.repos.UserRepository;
import io.bootify.backend.repos.OfferRepository;
import io.bootify.backend.repos.RentalApplicationRepository;
import io.bootify.backend.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;


@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final OfferRepository offerRepository;
    private final RentalApplicationRepository rentalApplicationRepository;


    public RentalService(final RentalRepository rentalRepository, 
                        final UserRepository userRepository, 
                        final OfferRepository offerRepository,
                        final RentalApplicationRepository rentalApplicationRepository) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
        this.offerRepository = offerRepository;
        this.rentalApplicationRepository = rentalApplicationRepository;
    }

    public List<RentalDTO> findAll() {
        final List<Rental> rentals = rentalRepository.findAll(Sort.by("id"));
        return rentals.stream()
                .map((rental) -> mapToDTO(rental, new RentalDTO()))
                .toList();
    }

    public Long getUserIdByOfferId(final Long offerId) {
        final Offer offer = offerRepository.findById(offerId)
                .orElseThrow(NotFoundException::new);
        return offer.getUser().getId();
    }

    public RentalDTO get(final Long id) {
        return rentalRepository.findById(id)
                .map((rental) -> mapToDTO(rental, new RentalDTO()))
                .orElseThrow(NotFoundException::new);
    }
    
    public Long create(final RentalDTO rentalDTO, CustomUserDetails customUserDetails) {
        final Rental rental = new Rental();
        mapToEntity(rentalDTO, rental);

        // Security check
        if (!("ROLE_ADMIN".equals(customUserDetails.getRole()) || customUserDetails.getId().equals(rental.getOffer().getUser().getId()))) {
            throw new AccessDeniedException("You don't have the permission to create this rental.");
        }

        return rentalRepository.save(rental).getId();
    }

    public void update(final Long id, final RentalDTO rentalDTO, CustomUserDetails customUserDetails) {
        final Rental rental = rentalRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        
        // Security check
        if (!("ROLE_ADMIN".equals(customUserDetails.getRole()) || customUserDetails.getId().equals(rental.getOffer().getUser().getId()))) {
            throw new AccessDeniedException("You don't have the permission to update this rental.");
        }

        mapToEntity(rentalDTO, rental);
        rentalRepository.save(rental);
    }




    public void delete(final Long id) {
        rentalRepository.deleteById(id);
    }

    private RentalDTO mapToDTO(final Rental rental, final RentalDTO rentalDTO) {
        rentalDTO.setId(rental.getId());
        rentalDTO.setRenterId(rental.getRenter().getId()); // fetch Id from User object
        rentalDTO.setOfferId(rental.getOffer().getId()); // fetch Id from Offer object
        rentalDTO.setStartDate(rental.getStartDate());
        rentalDTO.setEndDate(rental.getEndDate());
        rentalDTO.setIsFinished(rental.getIsFinished());
        return rentalDTO;
    }

    private Rental mapToEntity(final RentalDTO rentalDTO, final Rental rental) {
        rental.setRenter(userRepository.findById(rentalDTO.getRenterId()).orElseThrow(NotFoundException::new)); // fetch User by Id
        rental.setOffer(offerRepository.findById(rentalDTO.getOfferId()).orElseThrow(NotFoundException::new)); // fetch Offer by Id
        rental.setStartDate(rentalDTO.getStartDate());
        rental.setEndDate(rentalDTO.getEndDate());
        rental.setIsFinished(rentalDTO.getIsFinished());
        return rental;
    }

}

