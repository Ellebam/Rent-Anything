package io.bootify.backend.service;

import io.bootify.backend.domain.Offer;
import io.bootify.backend.domain.User;
import io.bootify.backend.model.OfferDTO;
import io.bootify.backend.repos.OfferRepository;
import io.bootify.backend.repos.UserRepository;
import io.bootify.backend.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final UserRepository userRepository;

    public OfferService(final OfferRepository offerRepository,
            final UserRepository userRepository) {
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
    }

    public List<OfferDTO> findAll() {
        final List<Offer> offers = offerRepository.findAll(Sort.by("id"));
        return offers.stream()
                .map((offer) -> mapToDTO(offer, new OfferDTO()))
                .toList();
    }

    public OfferDTO get(final Long id) {
        return offerRepository.findById(id)
                .map((offer) -> mapToDTO(offer, new OfferDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final OfferDTO offerDTO) {
        final Offer offer = new Offer();
        mapToEntity(offerDTO, offer);
        return offerRepository.save(offer).getId();
    }

    public void update(final Long id, final OfferDTO offerDTO) {
        final Offer offer = offerRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(offerDTO, offer);
        offerRepository.save(offer);
    }

    public void delete(final Long id) {
        offerRepository.deleteById(id);
    }

    private OfferDTO mapToDTO(final Offer offer, final OfferDTO offerDTO) {
        offerDTO.setId(offer.getId());
        offerDTO.setTitle(offer.getTitle());
        offerDTO.setDescription(offer.getDescription());
        offerDTO.setLocation(offer.getLocation());
        offerDTO.setPrice(offer.getPrice());
        offerDTO.setImageUrl(offer.getImageUrl());
        offerDTO.setUserId(offer.getUser() != null ? offer.getUser().getId() : null);
        return offerDTO;
    }

    private Offer mapToEntity(final OfferDTO offerDTO, final Offer offer) {
        offer.setTitle(offerDTO.getTitle());
        offer.setDescription(offerDTO.getDescription());
        offer.setLocation(offerDTO.getLocation());
        offer.setPrice(offerDTO.getPrice());
        offer.setImageUrl(offerDTO.getImageUrl());
        if (offerDTO.getUserId() != null) {
            final User user = userRepository.findById(offerDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));
            offer.setUser(user);
        } else {
            offer.setUser(null);
        }
        return offer;
    }

}
