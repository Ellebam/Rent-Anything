package io.bootify.backend;

import io.bootify.backend.domain.User;
import io.bootify.backend.repos.UserRepository;
import io.bootify.backend.service.UserService;
import io.bootify.backend.model.UserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateUser() {
        // Given
        User user = new User();
        user.setUsername("test");
        user.setPassword("password");
        user.setEmail("test@email.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setCanPostOffer(true); // you can adjust this according to your test

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("test");
        userDTO.setPassword("password");
        userDTO.setEmail("test@email.com");
        userDTO.setFirstName("Test");
        userDTO.setLastName("User");
        userDTO.setCanPostOffer(true); // you can adjust this according to your test

        given(userRepository.save(user)).willReturn(user);

        // When
        Long createdUserId = userService.create(userDTO);

        // Then
        assertThat(createdUserId).isEqualTo(user.getId());
    }
}
