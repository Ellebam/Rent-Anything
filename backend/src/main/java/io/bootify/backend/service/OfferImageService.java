package io.bootify.backend.service;

import io.bootify.backend.domain.Offer;
import io.bootify.backend.domain.OfferImage;
import io.bootify.backend.repos.OfferImageRepository;
import io.bootify.backend.repos.OfferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class OfferImageService {

    private final OfferImageRepository offerImageRepository;
    private final OfferRepository offerRepository;

    // Define the logger object for this class
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public OfferImageService(OfferImageRepository offerImageRepository, OfferRepository offerRepository) {
        this.offerImageRepository = offerImageRepository;
        this.offerRepository = offerRepository;
    }

    @Transactional
    public void saveImages(Long offerId, List<MultipartFile> images) {
        log.info("Attempting to save images for offer id: {}", offerId);
        // Get the offer for the given offerId. If the offer doesn't exist, throw an exception.
        Offer offer = offerRepository.findById(offerId)
            .orElseThrow(() -> {
                log.error("Offer not found for id: {}", offerId);
                return new IllegalArgumentException("Offer not found");
            });

        // Construct the path of the directory where the offer's images will be saved.
        String offerImagesDirectoryPath = "/app/images/" + offerId;
        File directory = new File(offerImagesDirectoryPath);

        // If the directory doesn't exist, create it.
        if (!directory.exists()) {
            log.info("Creating directory for images: {}", offerImagesDirectoryPath);
            directory.mkdirs();
        }

        // If images list is not empty
        if (images != null && !images.isEmpty()) {
            // Iterate over the images provided in the request.
            for (int i = 0; i < images.size(); i++) {
                MultipartFile image = images.get(i);
                String originalFilename = image.getOriginalFilename();

                // Security check: make sure the filename doesn't contain any harmful characters.
                if (originalFilename != null && originalFilename.matches("[a-zA-Z0-9_.-]*")) {
                    Path path = Paths.get(offerImagesDirectoryPath + "/" + originalFilename);

                    // Copy the image file to the directory.
                    try {
                        log.info("Copying image {} to directory", originalFilename);
                        Files.copy(image.getInputStream(), path);
                    } catch (IOException e) {
                        log.error("Error occurred while copying image to directory: {}", e.getMessage());
                    }

                    // Create an OfferImage entity for the image, set its properties, and save it to the repository.
                    OfferImage offerImage = new OfferImage(path.toString(), offer);
                    offerImage.setImageOrder(i+1);
                    offerImageRepository.save(offerImage);
                } else {
                    log.error("Invalid filename detected: {}", originalFilename);
                    throw new IllegalArgumentException("Invalid filename");
                }
            }
        }
    }

    @Transactional
    public void deleteImages(Long offerId, List<Long> imageIds) {
        log.info("Attempting to delete images for offer id: {}", offerId);
        // Check if imageIds list is not empty
        if (imageIds != null && !imageIds.isEmpty()) {
            for(Long imageId : imageIds) {
                OfferImage image = offerImageRepository.findById(imageId)
                    .orElseThrow(() -> {
                        log.error("Image not found for id: {}", imageId);
                        return new IllegalArgumentException("Image not found");
                    });
                if(!image.getOffer().getId().equals(offerId)) {
                    log.error("Image {} does not belong to offer {}", imageId, offerId);
                    throw new IllegalArgumentException("Image does not belong to this offer");
                }
                // Physically delete the image
                try {
                    log.info("Deleting image file: {}", image.getImagePath());
                    Files.delete(Paths.get(image.getImagePath()));
                } catch (IOException e) {
                    // Log the error and proceed to the next image
                    log.error("Error deleting image file: {}", e.getMessage());
                }
                // Delete the image entity
                offerImageRepository.delete(image);
            }
        }
    }

    @Transactional
    public void cleanupImages(Long offerId, List<Long> imageIdsToKeep) {
        log.info("Cleaning up images for offer id: {}", offerId);
        // Get the offer for the given offerId. If the offer doesn't exist, throw an exception.
        Offer offer = offerRepository.findById(offerId)
            .orElseThrow(() -> {
                log.error("Offer not found for id: {}", offerId);
                return new IllegalArgumentException("Offer not found");
            });

        // Get all the images for the given offerId
        List<OfferImage> offerImages = offer.getOfferImages();

        // Create a list to hold the IDs of images to delete
        List<Long> imageIdsToDelete = new ArrayList<>();

        // Iterate over the offer's images
        for (OfferImage image : offerImages) {
            // If the image is not in the provided list, add its ID to the deletion list
            if (!imageIdsToKeep.contains(image.getId())) {
                imageIdsToDelete.add(image.getId());
            }
        }

        // Delete the images not in the provided list
        deleteImages(offerId, imageIdsToDelete);
    }


    @Transactional
    public void updateImageOrder(Long offerId, List<Long> imageIds) {
        log.info("Updating image order for offer id: {}", offerId);
        // check if offer exists
        if (!offerRepository.existsById(offerId)) {
            log.error("Offer not found for id: {}", offerId);
            throw new IllegalArgumentException("Offer not found");
        }

        // retrieve and update images in the order they appear in the list
        for (int i = 0; i < imageIds.size(); i++) {
            Long imageId = imageIds.get(i);
            OfferImage image = offerImageRepository.findById(imageId)
                .orElseThrow(() -> {
                    log.error("Image not found for id: {}", imageId);
                    return new IllegalArgumentException("Image not found");
                });

            image.setImageOrder(i);
            offerImageRepository.save(image);
        }
    }
}
