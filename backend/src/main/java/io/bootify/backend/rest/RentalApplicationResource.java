package io.bootify.backend.rest;

import io.bootify.backend.model.RentalApplicationDTO;
import io.bootify.backend.service.RentalApplicationService;
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


@RestController
@RequestMapping(value = "/api/rentalApplications", produces = MediaType.APPLICATION_JSON_VALUE)
public class RentalApplicationResource {

    private final RentalApplicationService rentalApplicationService;

    public RentalApplicationResource(final RentalApplicationService rentalApplicationService) {
        this.rentalApplicationService = rentalApplicationService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<RentalApplicationDTO>> getAllRentalApplications() {
        return ResponseEntity.ok(rentalApplicationService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize(
    "@rentalApplicationService.get(#id).getApplicantId() == authentication.principal.id or "
        + "@rentalApplicationService.getOffererId(#id) == authentication.principal.id or "
        + "hasRole('ROLE_ADMIN')"
    )
    public ResponseEntity<RentalApplicationDTO> getRentalApplication(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(rentalApplicationService.get(id));
    }


    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createRentalApplication(
            @RequestBody @Valid final RentalApplicationDTO rentalApplicationDTO) {
        final Long createdId = rentalApplicationService.create(rentalApplicationDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@rentalApplicationService.get(#id).getApplicantId() == authentication.principal.id or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> updateRentalApplication(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final RentalApplicationDTO rentalApplicationDTO) {
        rentalApplicationService.update(id, rentalApplicationDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("@rentalApplicationService.getOffererId(#id) == authentication.principal.id or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> approveRentalApplication(@PathVariable(name = "id") final Long id) {
        rentalApplicationService.approve(id);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("@rentalApplicationService.get(#id).getApplicantId() == authentication.principal.id or hasRole('ROLE_ADMIN')")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteRentalApplication(@PathVariable(name = "id") final Long id) {
        rentalApplicationService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
