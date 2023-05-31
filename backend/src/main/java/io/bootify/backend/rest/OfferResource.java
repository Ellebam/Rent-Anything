package io.bootify.backend.rest;

import io.bootify.backend.model.OfferDTO;
import io.bootify.backend.service.OfferService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/offers", produces = MediaType.APPLICATION_JSON_VALUE)
public class OfferResource {

    private final OfferService offerService;

    public OfferResource(final OfferService offerService) {
        this.offerService = offerService;
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
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createOffer(@RequestBody @Valid final OfferDTO offerDTO) {
        final Long createdId = offerService.create(offerDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateOffer(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final OfferDTO offerDTO) {
        offerService.update(id, offerDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteOffer(@PathVariable(name = "id") final Long id) {
        offerService.delete(id);
        return ResponseEntity.noContent().build();
    }

}