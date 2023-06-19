package io.bootify.backend.service;

import io.bootify.backend.domain.Offer;
import io.bootify.backend.domain.RentalApplication;
import io.bootify.backend.domain.User;
import io.bootify.backend.model.RentalApplicationDTO;
import io.bootify.backend.repos.OfferRepository;
import io.bootify.backend.repos.RentalApplicationRepository;
import io.bootify.backend.repos.UserRepository;
import io.bootify.backend.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class RentalApplicationService {

    private final RentalApplicationRepository rentalApplicationRepository;
    private final UserRepository userRepository;
    private final OfferRepository offerRepository;

    public RentalApplicationService(final RentalApplicationRepository rentalApplicationRepository,
            final UserRepository userRepository, final OfferRepository offerRepository) {
        this.rentalApplicationRepository = rentalApplicationRepository;
        this.userRepository = userRepository;
        this.offerRepository = offerRepository;
    }

    public List<RentalApplicationDTO> findAll() {
        final List<RentalApplication> rentalApplications = rentalApplicationRepository.findAll(Sort.by("id"));
        return rentalApplications.stream()
                .map((rentalApplication) -> mapToDTO(rentalApplication, new RentalApplicationDTO()))
                .toList();
    }

    public RentalApplicationDTO get(final Long id) {
        return rentalApplicationRepository.findById(id)
                .map((rentalApplication) -> mapToDTO(rentalApplication, new RentalApplicationDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long getOffererId(final Long id) {
        final RentalApplication rentalApplication = rentalApplicationRepository.findByIdWithOfferAndUser(id)
                .orElseThrow(NotFoundException::new);
        return rentalApplication.getOffer().getUser().getId();
    }


    public Long create(final RentalApplicationDTO rentalApplicationDTO) {
        final RentalApplication rentalApplication = new RentalApplication();
        mapToEntity(rentalApplicationDTO, rentalApplication);
        return rentalApplicationRepository.save(rentalApplication).getId();
    }

    public void update(final Long id, final RentalApplicationDTO rentalApplicationDTO) {
        final RentalApplication rentalApplication = rentalApplicationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(rentalApplicationDTO, rentalApplication);
        rentalApplicationRepository.save(rentalApplication);
    }


    public void approve(final Long id) {
        final RentalApplication rentalApplication = rentalApplicationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        rentalApplication.setApproved(true);
        rentalApplicationRepository.save(rentalApplication);
    }

    public void delete(final Long id) {
        rentalApplicationRepository.deleteById(id);
    }
    private RentalApplicationDTO mapToDTO(final RentalApplication rentalApplication,
            final RentalApplicationDTO rentalApplicationDTO) {
        rentalApplicationDTO.setId(rentalApplication.getId());
        rentalApplicationDTO.setApplicantId(rentalApplication.getApplicant().getId());
        rentalApplicationDTO.setOfferId(rentalApplication.getOffer().getId());
        rentalApplicationDTO.setApproved(rentalApplication.isApproved());
        return rentalApplicationDTO;
    }

    private RentalApplication mapToEntity(final RentalApplicationDTO rentalApplicationDTO,
            final RentalApplication rentalApplication) {
        rentalApplication.setApproved(rentalApplicationDTO.isApproved());
        
        final User user = userRepository.findById(rentalApplicationDTO.getApplicantId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        rentalApplication.setApplicant(user);
        
        final Offer offer = offerRepository.findById(rentalApplicationDTO.getOfferId())
                .orElseThrow(() -> new NotFoundException("Offer not found"));
        rentalApplication.setOffer(offer);
        
        return rentalApplication;
    }
}
