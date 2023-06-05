package io.bootify.backend.service;

import io.bootify.backend.domain.User;
import io.bootify.backend.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Collections;

@Service  // Service class managed by Spring
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired  // Autowire userRepository, allowing Spring to automatically inject an instance
    private UserRepository userRepository;

    @Override
    // Load user details by username for authentication
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch user from database
        User user = userRepository.findByUsername(username);

        // If user is not found, throw an exception
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        
        // Create a list of authorities for the user, based on user's role
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        // Return a UserDetails object for authentication and access control
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}