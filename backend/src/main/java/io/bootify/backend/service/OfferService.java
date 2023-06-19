package io.bootify.backend.service;

import io.bootify.backend.domain.Offer;
import io.bootify.backend.domain.OfferImage;
import io.bootify.backend.domain.User;
import io.bootify.backend.model.OfferDTO;
import io.bootify.backend.repos.OfferRepository;
import io.bootify.backend.repos.UserRepository;
import io.bootify.backend.util.NotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final UserRepository userRepository;
    private final OfferImageService offerImageService;

    public OfferService(final OfferRepository offerRepository,
            final UserRepository userRepository, final OfferImageService offerImageService) {
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
        this.offerImageService = offerImageService;
    }

    @Transactional
    public List<OfferDTO> findAll() {
        final List<Offer> offers = offerRepository.findAll(Sort.by("id"));
        return offers.stream()
                .map((offer) -> mapToDTO(offer, new OfferDTO()))
                .toList();
    }

    @Transactional
    public OfferDTO get(final Long id) {
        return offerRepository.findById(id)
                .map((offer) -> mapToDTO(offer, new OfferDTO()))
                .orElseThrow(NotFoundException::new);
    }
    public Long create(final OfferDTO offerDTO) {
        if (offerDTO == null) {
            throw new IllegalArgumentException("OfferDTO cannot be null");
        }
        return this.create(offerDTO, null);
    }

    @Transactional
    public Long create(final OfferDTO offerDTO, List<MultipartFile> images) {
        if (offerDTO == null) {
            throw new IllegalArgumentException("OfferDTO cannot be null");
        }
        final Offer offer = new Offer();
        mapToEntity(offerDTO, offer);
        Long id = offerRepository.save(offer).getId();
    
        // Check if images list is not empty
        if (images != null && !images.isEmpty()) { 
            try {
                offerImageService.saveImages(id, images);
            } catch (IOException e) {
                throw new RuntimeException("Error while saving images", e);
            }
        }
        return id;
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
        List<String> imageUrls = offer.getOfferImages().stream()
                                       .map(OfferImage::getImagePath)
                                       .collect(Collectors.toList());
        offerDTO.setImageUrls(imageUrls);
        offerDTO.setUserId(offer.getUser() != null ? offer.getUser().getId() : null);
        offer.setIsDeactivated(offer.isDeactivated());
        return offerDTO;
    }

    private Offer mapToEntity(final OfferDTO offerDTO, final Offer offer) {
        offer.setTitle(offerDTO.getTitle());
        offer.setDescription(offerDTO.getDescription());
        offer.setLocation(offerDTO.getLocation());
        offer.setPrice(offerDTO.getPrice());
        List<OfferImage> offerImages = offerDTO.getImageUrls().stream()
                                               .map(url -> new OfferImage(url, offer))
                                               .collect(Collectors.toList());
        offer.setOfferImages(offerImages);
        if (offerDTO.getUserId() != null) {
            final User user = userRepository.findById(offerDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));
            offer.setUser(user);
        } else {
            offer.setUser(null);
        }
        offer.setIsDeactivated(offerDTO.isDeactivated());
        return offer;
    }

    @Transactional
    public void deactivate(Long id) {
        Offer offer = offerRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Offer not found"));
        
        offer.setIsDeactivated(true);
        offerRepository.save(offer);
}


}
