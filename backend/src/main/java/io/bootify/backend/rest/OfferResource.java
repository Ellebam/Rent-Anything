package io.bootify.backend.rest;

import io.bootify.backend.model.OfferDTO;
import io.bootify.backend.model.OfferImageDTO;
import io.bootify.backend.model.OfferRequestDTO;
import io.bootify.backend.service.OfferService;
import io.bootify.backend.service.OfferImageService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping(value = "/api/offers", produces = MediaType.APPLICATION_JSON_VALUE)
public class OfferResource {

    private final OfferService offerService;
    private final OfferImageService offerImageService;

    public OfferResource(final OfferService offerService, final OfferImageService offerImageService) {
        this.offerService = offerService;
        this.offerImageService = offerImageService;

    }

    @GetMapping
    public ResponseEntity<List<OfferDTO>> getAllOffers() {
        return ResponseEntity.ok(offerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferDTO> getOffer(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(offerService.get(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_POSTER') or hasRole('ROLE_ADMIN')")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createOffer(@ModelAttribute OfferRequestDTO offerRequestDTO) {
        final Long createdId = offerService.create(offerRequestDTO.getOfferDTO(), offerRequestDTO.getImages());
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PostMapping("/no-image")
    @PreAuthorize("hasRole('ROLE_POSTER') or hasRole('ROLE_ADMIN')")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createOfferWithoutImage(@RequestBody OfferDTO offerDTO) {
        final Long createdId = offerService.create(offerDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("@offerService.get(#id).getUserId() == authentication.principal.id or hasRole('ROLE_ADMIN')")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deactivateOffer(@PathVariable(name = "id") final Long id) {
        offerService.deactivate(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}")
    @PreAuthorize("@offerService.get(#id).getUserId() == authentication.principal.id or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> updateOffer(@PathVariable(name = "id") final Long id,
                                            @RequestBody @Valid final OfferDTO offerDTO) {
        offerService.update(id, offerDTO);
        return ResponseEntity.ok().build();
    }

    
    @GetMapping("/{id}/images")
    @PreAuthorize("@offerService.get(#id).getUserId() == authentication.principal.id or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<OfferImageDTO>> getOfferImages(@PathVariable Long id) {
        List<OfferImageDTO> offerImageDTOS = offerService.getOfferImages(id);
        return new ResponseEntity<>(offerImageDTOS, HttpStatus.OK);
    }

    @PatchMapping("/{id}/images")
    @PreAuthorize("@offerService.get(#id).getUserId() == authentication.principal.id or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> addOrDeleteImages(@PathVariable(name = "id") final Long id, 
                                                @ModelAttribute OfferRequestDTO offerRequestDTO) {
        List<MultipartFile> imagesToAdd = offerRequestDTO.getImages();
        List<Long> imageIdsToDelete = offerRequestDTO.getImageIdsToDelete();

        if (imagesToAdd != null && !imagesToAdd.isEmpty()) {
            offerImageService.saveImages(id, imagesToAdd);
        }

        if (imageIdsToDelete != null && !imageIdsToDelete.isEmpty()) {
            offerImageService.deleteImages(id, imageIdsToDelete);
        }

        offerImageService.cleanupImages(id, offerRequestDTO.getImageIdsToDelete());

        return ResponseEntity.ok().build();
    }



    @PutMapping("/{id}/image-order")
    @PreAuthorize("@offerService.get(#id).getUserId() == authentication.principal.id or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> updateImageOrder(@PathVariable(name = "id") final Long id, 
                                                @RequestBody List<Long> imageIds) {
        offerImageService.updateImageOrder(id, imageIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteOffer(@PathVariable(name = "id") final Long id) {
        offerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
