package io.bootify.backend.service;

import io.bootify.backend.domain.Message;
import io.bootify.backend.domain.Offer;
import io.bootify.backend.domain.OfferImage;
import io.bootify.backend.domain.User;
import io.bootify.backend.model.OfferDTO;
import io.bootify.backend.model.OfferImageDTO;
import io.bootify.backend.repos.MessageRepository;
import io.bootify.backend.repos.OfferRepository;
import io.bootify.backend.repos.UserRepository;
import io.bootify.backend.util.NotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final UserRepository userRepository;
    private final OfferImageService offerImageService;
    private final MessageRepository messageRepository;

    public OfferService(final OfferRepository offerRepository,
            final UserRepository userRepository, final OfferImageService offerImageService, final MessageRepository messageRepository) {
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
        this.offerImageService = offerImageService;
        this.messageRepository = messageRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger(OfferService.class);

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
            offerImageService.saveImages(id, images);
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
        Offer offer = offerRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Offer not found"));

        try {
            // Retrieve all messages associated with this offer
            List<Message> messages = messageRepository.findByOffer(offer);

            // For each message, set the offer field to null and save
            for (Message message : messages) {
                    try {
                message.setOffer(null);
                messageRepository.save(message);
            } catch (Exception e) {
                logger.error("Exception occurred while uncoupling message with id: " + message.getId(), e);
            }
        }
        } catch (Exception e) {
            logger.error("Exception occurred while fetching messages", e);
        }


        // Delete associated images physically
        String offerImagesDirectoryPath = "/app/images/" + id;
        File directory = new File(offerImagesDirectoryPath);
        deleteDirectory(directory);

        // Delete Offer entity. This should also delete OfferImage, RentalApplication, Rental entities due to CascadeType.ALL
        offerRepository.delete(offer);
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

    private void deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        if (!directoryToBeDeleted.delete()) {
            logger.error("Could not delete directory " + directoryToBeDeleted);
        }
    }

    public List<OfferImageDTO> getOfferImages(Long offerId) {
    Offer offer = offerRepository.findById(offerId)
            .orElseThrow(() -> new NotFoundException("Offer not found"));

    return offer.getOfferImages().stream()
            .map(this::mapOfferImageToDTO)
            .collect(Collectors.toList());
    }

    private OfferImageDTO mapOfferImageToDTO(OfferImage offerImage) {
        OfferImageDTO offerImageDTO = new OfferImageDTO();
        offerImageDTO.setId(offerImage.getId());
        offerImageDTO.setImagePath(offerImage.getImagePath());
        offerImageDTO.setImageOrder(offerImage.getImageOrder());
        offerImageDTO.setOfferId(offerImage.getOffer().getId());
        return offerImageDTO;
    }

}
