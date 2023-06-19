package io.bootify.backend.rest;

import io.bootify.backend.model.RentalDTO;
import io.bootify.backend.service.CustomUserDetails;
import io.bootify.backend.service.RentalService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.AccessDeniedException;




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
    @PreAuthorize("hasRole('ROLE_POSTER') or hasRole('ROLE_ADMIN')")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createRental(@RequestBody @Valid final RentalDTO rentalDTO, Authentication authentication) {
        CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
        final Long createdId = rentalService.create(rentalDTO, currentUser);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_POSTER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> updateRental(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final RentalDTO rentalDTO, Authentication authentication) {
        CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
        rentalService.update(id, rentalDTO, currentUser);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_POSTER')")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteRental(@PathVariable(name = "id") final Long id, Authentication authentication) {
        CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
        final RentalDTO rentalDTO = rentalService.get(id);

        // Get the userId of the offer associated with this rental
        Long offerUserId = rentalService.getUserIdByOfferId(rentalDTO.getOfferId());

        // Security check
        if (!("ROLE_ADMIN".equals(currentUser.getRole())) || currentUser.getId().equals(offerUserId)) {
            throw new AccessDeniedException("You don't have the permission to delete this rental.");
        }

        rentalService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
