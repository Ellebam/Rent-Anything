package io.bootify.backend;

import io.bootify.backend.domain.Role;
import io.bootify.backend.domain.User;
import io.bootify.backend.repos.UserRepository;
import io.bootify.backend.service.UserService;
import io.bootify.backend.model.UserDTO;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateUser() {
        // Given
        // Setup test data
        User user = new User();
        user.setUsername("test");
        user.setPassword("password");
        user.setEmail("test@email.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setRole(Role.POSTER); 
    
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("test");
        userDTO.setPassword("password");
        userDTO.setEmail("test@email.com");
        userDTO.setFirstName("Test");
        userDTO.setLastName("User");
        userDTO.setRole(Role.POSTER); 

        // Mock the behavior of passwordEncoder.encode() method
        given(passwordEncoder.encode(any(String.class))).willAnswer(invocation -> invocation.getArgument(0));
    
        // Mock the behavior of userRepository.save() method to return our user
        given(userRepository.save(any(User.class))).willReturn(user);
    
        // When
        // Call the method we are testing
        Long createdUserId = userService.create(userDTO);
    
        // Then
        // Capture the argument passed to userRepository.save() method
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
    
        // Retrieve the argument (User object) that was passed to userRepository.save()
        User capturedUser = userArgumentCaptor.getValue();
    
        // Assert that the argument's fields have the expected values
        assertThat(capturedUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(capturedUser.getPassword()).isEqualTo(user.getPassword());
        assertThat(capturedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(capturedUser.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(capturedUser.getLastName()).isEqualTo(user.getLastName());
        assertThat(capturedUser.getCanPostOffer()).isEqualTo(user.getCanPostOffer());
        assertThat(capturedUser.getRole()).isEqualTo(user.getRole());
    
        // Assert that the returned user ID matches the expected value (this might need to be adapted depending on how you deal with IDs)
        assertThat(createdUserId).isEqualTo(user.getId());
    }
}
