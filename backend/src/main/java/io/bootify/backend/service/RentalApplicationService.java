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

    public void delete(final Long id) {
        rentalApplicationRepository.deleteById(id);
    }

    private RentalApplicationDTO mapToDTO(final RentalApplication rentalApplication,
            final RentalApplicationDTO rentalApplicationDTO) {
        rentalApplicationDTO.setId(rentalApplication.getId());
        rentalApplicationDTO.setUserId(rentalApplication.getUserId());
        rentalApplicationDTO.setOfferId(rentalApplication.getOfferId());
        rentalApplicationDTO.setIsApproved(rentalApplication.getIsApproved());
        rentalApplicationDTO.setUser(rentalApplication.getUser() == null ? null : rentalApplication.getUser().getId());
        rentalApplicationDTO.setOffer(rentalApplication.getOffer() == null ? null : rentalApplication.getOffer().getId());
        return rentalApplicationDTO;
    }

    private RentalApplication mapToEntity(final RentalApplicationDTO rentalApplicationDTO,
            final RentalApplication rentalApplication) {
        rentalApplication.setUserId(rentalApplicationDTO.getUserId());
        rentalApplication.setOfferId(rentalApplicationDTO.getOfferId());
        rentalApplication.setIsApproved(rentalApplicationDTO.getIsApproved());
        final User user = rentalApplicationDTO.getUser() == null ? null : userRepository.findById(rentalApplicationDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        rentalApplication.setUser(user);
        final Offer offer = rentalApplicationDTO.getOffer() == null ? null : offerRepository.findById(rentalApplicationDTO.getOffer())
                .orElseThrow(() -> new NotFoundException("offer not found"));
        rentalApplication.setOffer(offer);
        return rentalApplication;
    }

}
