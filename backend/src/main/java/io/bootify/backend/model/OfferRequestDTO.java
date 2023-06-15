package io.bootify.backend.model;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public class OfferRequestDTO {

    private OfferDTO offerDTO;
    private List<MultipartFile> images;

    public OfferDTO getOfferDTO() {
        return offerDTO;
    }

    public void setOfferDTO(OfferDTO offerDTO) {
        this.offerDTO = offerDTO;
    }

    public List<MultipartFile> getImages() {
        return images;
    }

    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }
}
