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

        /**
     * Updates the user with the given id using the provided UserUpdateDTO object.
     *
     * If the current user is ADMIN, they can update almost any field of any user. 
     * If the current user is not an ADMIN, they can only update their own information,
     * specifically their email, first name, last name, and password.
     *
     * @param id the id of the user to update
     * @param userUpdateDTO the UserUpdateDTO object containing the new user information
     * @throws NotFoundException if a user with the given id is not found
     * @throws AccessDeniedException if the current user is not authorized to update the specified user
     * @throws IllegalArgumentException if a non-ADMIN user attempts to update fields they are not allowed to
     */
    public void update(final Long id, final UserUpdateDTO userUpdateDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        String currentRole = currentUser.getRole().replace("ROLE_", "");
        
        if (Role.valueOf(currentRole) == Role.ADMIN || currentUser.getId().equals(user.getId())) {
            if (Role.valueOf(currentRole) == Role.ADMIN) {
                updateUserFields(user, userUpdateDTO, true);
            } else {
                if(userUpdateDTO.getUsername() != null || userUpdateDTO.getRole() != null || userUpdateDTO.getCanPostOffer() != null) {
                    throw new IllegalArgumentException("You are not allowed to update username, role or canPostOffer fields");
                }
                updateUserFields(user, userUpdateDTO, false);
            }
            userRepository.save(user);
        } else {
            throw new AccessDeniedException("You don't have permission to perform this operation");
        }
    }
    
    private void updateUserFields(User user, UserUpdateDTO userUpdateDTO, boolean isAdmin) {
        if(userUpdateDTO.getUsername() != null && isAdmin) user.setUsername(userUpdateDTO.getUsername());
        if(userUpdateDTO.getEmail() != null) user.setEmail(userUpdateDTO.getEmail());
        if(userUpdateDTO.getFirstName() != null) user.setFirstName(userUpdateDTO.getFirstName());
        if(userUpdateDTO.getLastName() != null) user.setLastName(userUpdateDTO.getLastName());
        if(userUpdateDTO.getPassword() != null) user.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
        if(userUpdateDTO.getRole() != null && isAdmin) user.setRole(userUpdateDTO.getRole());
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
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
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
