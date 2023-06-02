package io.bootify.backend;

import io.bootify.backend.domain.RentalApplication;
import io.bootify.backend.domain.User;
import io.bootify.backend.domain.Offer;
import io.bootify.backend.repos.RentalApplicationRepository;
import io.bootify.backend.repos.UserRepository;
import io.bootify.backend.repos.OfferRepository;
import io.bootify.backend.service.RentalApplicationService;
import io.bootify.backend.model.RentalApplicationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.lenient;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

// Annotation to integrate Mockito in JUnit 5 tests
@ExtendWith(MockitoExtension.class)
class RentalApplicationServiceTest {

    // Mocking the repositories used by RentalApplicationService
    @Mock
    private RentalApplicationRepository rentalApplicationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OfferRepository offerRepository;

    // This annotation injects the mocked dependencies into the RentalApplicationService
    @InjectMocks
    private RentalApplicationService rentalApplicationService;

    private RentalApplication rentalApplication;
    private RentalApplicationDTO rentalApplicationDTO;

    // This method is called before each test. It prepares the data to be used in tests
    @BeforeEach
    void setup() {
        // Create dummy user and offer
        User user = new User();
        user.setId(1L);

        Offer offer = new Offer();
        offer.setId(1L);
        
        // We use lenient here because we want to set a default behavior that may not be used in all the tests
        lenient().when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        lenient().when(offerRepository.findById(any(Long.class))).thenReturn(Optional.of(offer));
        
        // Create a dummy RentalApplication
        rentalApplication = new RentalApplication();
        rentalApplication.setId(1L);
        rentalApplication.setApplicant(user);
        rentalApplication.setOffer(offer);
        rentalApplication.setApproved(true);

        // Create a dummy RentalApplicationDTO
        rentalApplicationDTO = new RentalApplicationDTO();
        rentalApplicationDTO.setApplicantId(1L);
        rentalApplicationDTO.setOfferId(1L);
        rentalApplicationDTO.setApproved(true);
    }

    // This test verifies if the create method in RentalApplicationService is working as expected
    @Test
    void shouldCreateRentalApplication() {
        // We tell Mockito to return our dummy RentalApplication when save method is called on the repository
        given(rentalApplicationRepository.save(any(RentalApplication.class))).willReturn(rentalApplication);

        Long createdRentalApplicationId = rentalApplicationService.create(rentalApplicationDTO);

        ArgumentCaptor<RentalApplication> rentalApplicationArgumentCaptor = ArgumentCaptor.forClass(RentalApplication.class);
        verify(rentalApplicationRepository).save(rentalApplicationArgumentCaptor.capture());
        RentalApplication capturedRentalApplication = rentalApplicationArgumentCaptor.getValue();

        assertThat(capturedRentalApplication.getApplicant().getId()).isEqualTo(rentalApplicationDTO.getApplicantId());
        assertThat(capturedRentalApplication.getOffer().getId()).isEqualTo(rentalApplicationDTO.getOfferId());
        assertThat(capturedRentalApplication.isApproved()).isEqualTo(rentalApplicationDTO.isApproved());

        assertThat(createdRentalApplicationId).isEqualTo(rentalApplication.getId());
    }

    // This test verifies if the update method in RentalApplicationService is working as expected
    @Test
    void shouldUpdateRentalApplication() {
        // We tell Mockito to return our dummy RentalApplication when findById method is called on the repository
        given(rentalApplicationRepository.findById(any(Long.class))).willReturn(Optional.of(rentalApplication));
        // We also tell Mockito to return our dummy RentalApplication when save method is called on the repository
        given(rentalApplicationRepository.save(any(RentalApplication.class))).willReturn(rentalApplication);

        rentalApplicationService.update(1L, rentalApplicationDTO);

        ArgumentCaptor<RentalApplication> rentalApplicationArgumentCaptor = ArgumentCaptor.forClass(RentalApplication.class);
        verify(rentalApplicationRepository).save(rentalApplicationArgumentCaptor.capture());
        RentalApplication capturedRentalApplication = rentalApplicationArgumentCaptor.getValue();

        assertThat(capturedRentalApplication.getId()).isEqualTo(1L);
        assertThat(capturedRentalApplication.getApplicant().getId()).isEqualTo(rentalApplicationDTO.getApplicantId());
        assertThat(capturedRentalApplication.getOffer().getId()).isEqualTo(rentalApplicationDTO.getOfferId());
        assertThat(capturedRentalApplication.isApproved()).isEqualTo(rentalApplicationDTO.isApproved());
    }
}