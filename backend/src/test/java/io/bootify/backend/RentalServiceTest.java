package io.bootify.backend;

import io.bootify.backend.domain.Rental;
import io.bootify.backend.domain.User;
import io.bootify.backend.domain.Offer;
import io.bootify.backend.repos.RentalRepository;
import io.bootify.backend.repos.UserRepository;
import io.bootify.backend.repos.OfferRepository;
import io.bootify.backend.service.RentalService;
import io.bootify.backend.model.RentalDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * Unit test for the RentalService class.
 * It uses Mockito to mock the dependencies (RentalRepository, UserRepository, and OfferRepository).
 */
@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OfferRepository offerRepository;

    @InjectMocks
    private RentalService rentalService;

    private Rental rental;
    private RentalDTO rentalDTO;

    /**
     * This method is executed before each test. 
     * It initializes the Rental and RentalDTO objects that are used in the test methods.
     */
    @BeforeEach
    void setup() {
        // Initialize the User object
        User user = new User();
        user.setId(1L);

        // Initialize the Offer object
        Offer offer = new Offer();
        offer.setId(1L);

        // Initialize the Rental object
        rental = new Rental();
        rental.setId(1L);
        rental.setRenter(user);
        rental.setOffer(offer);
        rental.setStartDate(OffsetDateTime.now());
        rental.setEndDate(OffsetDateTime.now().plusDays(7));

        // Initialize the RentalDTO object
        rentalDTO = new RentalDTO();
        rentalDTO.setRenterId(1L);
        rentalDTO.setOfferId(1L);
        rentalDTO.setStartDate(OffsetDateTime.now().plusDays(10));
        rentalDTO.setEndDate(OffsetDateTime.now().plusDays(17));
    }

    /**
     * Test case for the create method of RentalService.
     * This test verifies that the create method calls the save method of RentalRepository 
     * and that the Rental object saved to the repository has the correct values.
     */
    @Test
    void shouldCreateRental() {
        // Given
        given(rentalRepository.save(any(Rental.class))).willReturn(rental);

        // When
        Long createdRentalId = rentalService.create(rentalDTO);

        // Then
        ArgumentCaptor<Rental> rentalArgumentCaptor = ArgumentCaptor.forClass(Rental.class);
        verify(rentalRepository).save(rentalArgumentCaptor.capture());
        Rental capturedRental = rentalArgumentCaptor.getValue();

        assertThat(capturedRental.getRenter().getId()).isEqualTo(rentalDTO.getRenterId());
        assertThat(capturedRental.getOffer().getId()).isEqualTo(rentalDTO.getOfferId());
        assertThat(capturedRental.getStartDate()).isEqualTo(rentalDTO.getStartDate());
        assertThat(capturedRental.getEndDate()).isEqualTo(rentalDTO.getEndDate());

        assertThat(createdRentalId).isEqualTo(rental.getId());
    }

    /**
     * Test case for the update method of RentalService.
     * This test verifies that the update method calls the save method of RentalRepository 
     * and that the Rental object saved to the repository has the correct values.
     */
    @Test
    void shouldUpdateRental() {
        // Given
        given(rentalRepository.findById(any(Long.class))).willReturn(Optional.of(rental));
        given(rentalRepository.save(any(Rental.class))).willReturn(rental);

        // When
        rentalService.update(1L, rentalDTO);

        // Then
        ArgumentCaptor<Rental> rentalArgumentCaptor = ArgumentCaptor.forClass(Rental.class);
        verify(rentalRepository).save(rentalArgumentCaptor.capture());
        Rental capturedRental = rentalArgumentCaptor.getValue();

        assertThat(capturedRental.getRenter().getId()).isEqualTo(rentalDTO.getRenterId());
        assertThat(capturedRental.getOffer().getId()).isEqualTo(rentalDTO.getOfferId());
        assertThat(capturedRental.getStartDate()).isEqualTo(rentalDTO.getStartDate());
        assertThat(capturedRental.getEndDate()).isEqualTo(rentalDTO.getEndDate());
    }

    /**
     * Test case for the delete method of RentalService.
     * This test verifies that the delete method calls the deleteById method of RentalRepository 
     * with the correct id.
     */
    @Test
    void shouldDeleteRental() {
        // No setup needed, test the service method

        // When
        rentalService.delete(1L);

        // Then
        verify(rentalRepository).deleteById(1L);
    }

    /**
     * Test case for the get method of RentalService.
     * This test verifies that the get method calls the findById method of RentalRepository 
     * and that the returned RentalDTO has the correct values.
     */
    @Test
    void shouldFindRentalById() {
        // Given
        given(rentalRepository.findById(any(Long.class))).willReturn(Optional.of(rental));

        // When
        RentalDTO foundRental = rentalService.get(1L);

        // Then
        assertThat(foundRental.getRenterId()).isEqualTo(rental.getRenter().getId());
        assertThat(foundRental.getOfferId()).isEqualTo(rental.getOffer().getId());
        assertThat(foundRental.getStartDate()).isEqualTo(rental.getStartDate());
        assertThat(foundRental.getEndDate()).isEqualTo(rental.getEndDate());
    }
}