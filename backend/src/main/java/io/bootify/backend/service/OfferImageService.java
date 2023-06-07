

package io.bootify.backend.service;

import io.bootify.backend.domain.Offer;
import io.bootify.backend.domain.OfferImage;
import io.bootify.backend.repos.OfferImageRepository;
import io.bootify.backend.repos.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
        Offer offer = offerRepository.findById(offerId)
            .orElseThrow(() -> new IllegalArgumentException("Offer not found"));
    
        for (int i = 0; i < images.size(); i++) {
            MultipartFile image = images.get(i);
            Path path = Paths.get("/app/images/" + image.getOriginalFilename());
            Files.copy(image.getInputStream(), path);

            OfferImage offerImage = new OfferImage(path.toString(), offer);  // updated this line to use the constructor
            offerImage.setImageOrder(i);

            offerImageRepository.save(offerImage);
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