

package io.bootify.backend.service;

import io.bootify.backend.domain.Offer;
import io.bootify.backend.domain.OfferImage;
import io.bootify.backend.repos.OfferImageRepository;
import io.bootify.backend.repos.OfferRepository;
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

    @Autowired
    public OfferImageService(OfferImageRepository offerImageRepository, OfferRepository offerRepository) {
        this.offerImageRepository = offerImageRepository;
        this.offerRepository = offerRepository;
    }
    @Transactional
    public void saveImages(Long offerId, List<MultipartFile> images) throws IOException {
        // Get the offer for the given offerId. If the offer doesn't exist, throw an exception.
        Offer offer = offerRepository.findById(offerId)
            .orElseThrow(() -> new IllegalArgumentException("Offer not found"));
        
        // Construct the path of the directory where the offer's images will be saved.
        String offerImagesDirectoryPath = "/app/images/" + offerId;
        File directory = new File(offerImagesDirectoryPath);

        // If the directory doesn't exist, create it.
        if (!directory.exists()) {
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
                    Files.copy(image.getInputStream(), path);
                    
                    // Create an OfferImage entity for the image, set its properties, and save it to the repository.
                    OfferImage offerImage = new OfferImage(path.toString(), offer);
                    offerImage.setImageOrder(i+1);
                    offerImageRepository.save(offerImage);
                } else {
                    throw new IllegalArgumentException("Invalid filename");
                }
            }
        }
    }
    
    
    @Transactional
    public void deleteImages(Long offerId, List<Long> imageIds) {
        // Check if imageIds list is not empty
        if (imageIds != null && !imageIds.isEmpty()) {
            for(Long imageId : imageIds) {
                OfferImage image = offerImageRepository.findById(imageId)
                    .orElseThrow(() -> new IllegalArgumentException("Image not found"));
                if(!image.getOffer().getId().equals(offerId)) {
                    throw new IllegalArgumentException("Image does not belong to this offer");
                }
                // Physically delete the image
                try {
                    Files.delete(Paths.get(image.getImagePath()));
                } catch (IOException e) {
                    // Log and handle the error as you prefer
                    System.err.println("Error deleting image file: " + image.getImagePath());
                }
                // Delete the image entity
                offerImageRepository.delete(image);
            }
        }
    }

    @Transactional
    public void cleanupImages(Long offerId, List<Long> imageIdsToKeep) {
        // Get the offer for the given offerId. If the offer doesn't exist, throw an exception.
        Offer offer = offerRepository.findById(offerId)
            .orElseThrow(() -> new IllegalArgumentException("Offer not found"));

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
        // check if offer exists
        if (!offerRepository.existsById(offerId)) {
            throw new IllegalArgumentException("Offer not found");
        }
    
        // retrieve and update images in the order they appear in the list
        for (int i = 0; i < imageIds.size(); i++) {
            Long imageId = imageIds.get(i);
            OfferImage image = offerImageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("Image not found"));
    
            image.setImageOrder(i);
            offerImageRepository.save(image);
        }
    }
}