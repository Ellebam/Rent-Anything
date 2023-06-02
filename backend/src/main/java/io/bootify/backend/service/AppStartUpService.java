package io.bootify.backend.service;

import io.bootify.backend.model.UserDTO;
import io.bootify.backend.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;


@Service
public class AppStartUpService {

    @Autowired
    private UserService userService;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.firstName}")
    private String adminFirstName;

    @Value("${admin.lastName}")
    private String adminLastName;

    @Value("${admin.canPostOffer}")
    private Boolean adminCanPostOffer;

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent() {
        createAdminUserIfNotExists();
    }

    private void createAdminUserIfNotExists() {
        if (!userService.usernameExists(adminUsername)) {
            UserDTO adminUserDTO = new UserDTO();
            adminUserDTO.setUsername(adminUsername);
            adminUserDTO.setPassword(adminPassword);
            adminUserDTO.setEmail(adminEmail);
            adminUserDTO.setFirstName(adminFirstName);
            adminUserDTO.setLastName(adminLastName);
            adminUserDTO.setCanPostOffer(adminCanPostOffer);
            adminUserDTO.setRole(Role.ADMIN);
            userService.create(adminUserDTO);
        }
    }
}
