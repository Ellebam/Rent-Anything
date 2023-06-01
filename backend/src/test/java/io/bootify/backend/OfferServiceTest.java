package io.bootify.backend;

import io.bootify.backend.domain.Offer;
import io.bootify.backend.domain.User;
import io.bootify.backend.repos.OfferRepository;
import io.bootify.backend.repos.UserRepository;
import io.bootify.backend.service.OfferService;
import io.bootify.backend.model.OfferDTO;
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

import java.math.BigDecimal;
import java.util.Optional;

// Define test class with Mockito extension
@ExtendWith(MockitoExtension.class)
class OfferServiceTest {

    // Mock dependencies
    @Mock
    private OfferRepository offerRepository;

    @Mock
    private UserRepository userRepository;

    // Inject the service under test
    @InjectMocks
    private OfferService offerService;

    private Offer offer;
    private OfferDTO offerDTO;

    // Setup method, to be run before each test
    @BeforeEach
    void setup() {
        // Initialize the User object
        User user = new User();
        user.setId(1L);

        // Initialize the Offer object
        offer = new Offer();
        offer.setId(1L);
        offer.setTitle("Test offer");
        offer.setDescription("This is a test offer");
        offer.setLocation("Test location");
        offer.setPrice(new BigDecimal("999.99"));
        offer.setImageUrl("http://test.com/image.jpg");
        offer.setUser(user);

        // Initialize the OfferDTO object
        offerDTO = new OfferDTO();
        offerDTO.setTitle("New test offer");
        offerDTO.setDescription("This is a new test offer");
        offerDTO.setLocation("New test location");
        offerDTO.setPrice(new BigDecimal("1999.99"));
        offerDTO.setImageUrl("http://newtest.com/image.jpg");
    }
    // Test that Offer is correctly created
    @Test
    void shouldCreateOffer() {
        // Set up mock responses and test the service method

        given(offerRepository.save(any(Offer.class))).willReturn(offer);


        Long createdOfferId = offerService.create(offerDTO);

        ArgumentCaptor<Offer> offerArgumentCaptor = ArgumentCaptor.forClass(Offer.class);
        verify(offerRepository).save(offerArgumentCaptor.capture());
        Offer capturedOffer = offerArgumentCaptor.getValue();

        // Check that the saved offer matches the data we expect
        assertThat(capturedOffer.getTitle()).isEqualTo(offerDTO.getTitle());
        assertThat(capturedOffer.getDescription()).isEqualTo(offerDTO.getDescription());
        assertThat(capturedOffer.getLocation()).isEqualTo(offerDTO.getLocation());
        assertThat(capturedOffer.getPrice()).isEqualTo(offerDTO.getPrice());
        assertThat(capturedOffer.getImageUrl()).isEqualTo(offerDTO.getImageUrl());

        assertThat(createdOfferId).isEqualTo(offer.getId());
    }
    
    // Test that Offer is correctly updated
    @Test
    void shouldUpdateOffer() {
        // Set up mock responses and test the service method
        given(offerRepository.findById(any(Long.class))).willReturn(Optional.of(offer));
        given(offerRepository.save(any(Offer.class))).willReturn(offer);

        // Update custom field
        offerService.update(1L, offerDTO);

        // Check that the saved offer matches the updated data
        ArgumentCaptor<Offer> offerArgumentCaptor = ArgumentCaptor.forClass(Offer.class);
        verify(offerRepository).save(offerArgumentCaptor.capture());
        Offer capturedOffer = offerArgumentCaptor.getValue();

        assertThat(capturedOffer.getTitle()).isEqualTo(offerDTO.getTitle());
        assertThat(capturedOffer.getDescription()).isEqualTo(offerDTO.getDescription());
        assertThat(capturedOffer.getLocation()).isEqualTo(offerDTO.getLocation());
        assertThat(capturedOffer.getPrice()).isEqualTo(offerDTO.getPrice());
        assertThat(capturedOffer.getImageUrl()).isEqualTo(offerDTO.getImageUrl());
    }
    // Test that Offer is correctly deleted
    @Test
    void shouldDeleteOffer() {
   
        // No setup needed, test the service method
        offerService.delete(1L);

        // Verify that the repository's deleteById method was called with the right argument
        verify(offerRepository).deleteById(1L);
    }
    // Test that Offer is correctly found by Id
    @Test
    void shouldFindOfferById() {
        // Set up mock responses and test the service method
        given(offerRepository.findById(any(Long.class))).willReturn(Optional.of(offer));

        OfferDTO foundOfferDTO = offerService.get(1L);

        // Check that the found offer matches the data we expect
        assertThat(foundOfferDTO.getTitle()).isEqualTo(offer.getTitle());
        assertThat(foundOfferDTO.getDescription()).isEqualTo(offer.getDescription());
        assertThat(foundOfferDTO.getLocation()).isEqualTo(offer.getLocation());
        assertThat(foundOfferDTO.getPrice()).isEqualTo(offer.getPrice());
        assertThat(foundOfferDTO.getImageUrl()).isEqualTo(offer.getImageUrl());
    }
}
