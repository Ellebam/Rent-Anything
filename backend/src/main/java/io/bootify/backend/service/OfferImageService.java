

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