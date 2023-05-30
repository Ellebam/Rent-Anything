package io.bootify.backend.rest;

import io.bootify.backend.model.RentalDTO;
import io.bootify.backend.service.RentalService;
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
@RequestMapping(value = "/api/rentals", produces = MediaType.APPLICATION_JSON_VALUE)
public class RentalResource {

    private final RentalService rentalService;

    public RentalResource(final RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    public ResponseEntity<List<RentalDTO>> getAllRentals() {
        return ResponseEntity.ok(rentalService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalDTO> getRental(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(rentalService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createRental(@RequestBody @Valid final RentalDTO rentalDTO) {
        final Long createdId = rentalService.create(rentalDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRental(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final RentalDTO rentalDTO) {
        rentalService.update(id, rentalDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteRental(@PathVariable(name = "id") final Long id) {
        rentalService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
