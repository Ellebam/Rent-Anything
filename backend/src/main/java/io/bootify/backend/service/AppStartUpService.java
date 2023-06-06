package io.bootify.backend.service;

import io.bootify.backend.model.UserDTO;
import io.bootify.backend.config.PreconfiguredUsers;
import io.bootify.backend.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// A service class that performs operations upon application startup.
@Service
public class AppStartUpService {

    // Logger for this class.
    private static final Logger LOGGER = LoggerFactory.getLogger(AppStartUpService.class);

    // Autowired UserService to interact with user data.
    @Autowired
    private UserService userService;

    // Container of preconfigured users to be created at startup.
    private final PreconfiguredUsers preconfiguredUsers;

    // Constructor for AppStartUpService. Initializes the preconfigured users.
    public AppStartUpService(PreconfiguredUsers preconfiguredUsers) {
        LOGGER.info("Initializing preconfigured users at startup.");
        this.preconfiguredUsers = preconfiguredUsers;
    }

    // This method is invoked when the application context is refreshed. It creates preconfigured users if they do not already exist.
    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent() {
        preconfiguredUsers.getInitial().forEach(user -> 
            createUserIfNotExists(user.getUsername(), user.getPassword(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getCanPostOffer(), user.getRole()));
    }

    // This private method creates a user if it does not already exist.
    private void createUserIfNotExists(String username, String password, String email, String firstName, String lastName, Boolean canPostOffer, Role role) {
        if (!userService.usernameExists(username)) {
            LOGGER.info("Creating preconfigured user: {}", username);
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setPassword(password);
            userDTO.setEmail(email);
            userDTO.setFirstName(firstName);
            userDTO.setLastName(lastName);
            userDTO.setCanPostOffer(canPostOffer);
            userDTO.setRole(role);
            userService.create(userDTO);
        }
    }
}
