package io.bootify.backend.service;

import io.bootify.backend.domain.User;
import io.bootify.backend.model.UserDTO;
import io.bootify.backend.model.UserUpdateDTO;
import io.bootify.backend.repos.UserRepository;
import io.bootify.backend.util.NotFoundException;
import io.bootify.backend.domain.Role;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;





@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(final UserRepository userRepository, final BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("id"));
        return users.stream()
                .map((user) -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final Long id) {
        return userRepository.findById(id)
                .map((user) -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user).getId();
    }

    public void update(final Long id, final UserUpdateDTO userUpdateDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    
        // Get the role from the currentUser and remove the prefix "ROLE_"
        String currentRole = currentUser.getRole().replace("ROLE_", "");
    
        if (currentRole.equals(user.getRole().name()) || currentUser.getId().equals(user.getId())) {
            if (Role.valueOf(currentRole) == Role.ADMIN) {
                if(userUpdateDTO.getUsername() != null) user.setUsername(userUpdateDTO.getUsername());
                if(userUpdateDTO.getEmail() != null) user.setEmail(userUpdateDTO.getEmail());
                if(userUpdateDTO.getFirstName() != null) user.setFirstName(userUpdateDTO.getFirstName());
                if(userUpdateDTO.getLastName() != null) user.setLastName(userUpdateDTO.getLastName());
                if(userUpdateDTO.getPassword() != null) user.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
                if(userUpdateDTO.getRole() != null) user.setRole(userUpdateDTO.getRole());
                if(userUpdateDTO.getCanPostOffer() != null) user.setCanPostOffer(userUpdateDTO.getCanPostOffer());
            } else if (!currentUser.getRole().equals(Role.ADMIN.name())) {
                if(userUpdateDTO.getEmail() != null) user.setEmail(userUpdateDTO.getEmail());
                if(userUpdateDTO.getFirstName() != null) user.setFirstName(userUpdateDTO.getFirstName());
                if(userUpdateDTO.getLastName() != null) user.setLastName(userUpdateDTO.getLastName());
                if(userUpdateDTO.getPassword() != null) user.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
            }
            userRepository.save(user);
        } else {
            throw new AccessDeniedException("You don't have permission to perform this operation");
        }
    }
    
    

    

    public void delete(final Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setCanPostOffer(user.getCanPostOffer());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setCanPostOffer(userDTO.getCanPostOffer());
        user.setRole(userDTO.getRole());
        return user;
    }

    public boolean usernameExists(final String username) {
        return userRepository.existsByUsernameIgnoreCase(username);
    }

    public boolean authenticate(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
    
}
