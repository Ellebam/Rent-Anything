package io.bootify.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration  // Configuration class that provides beans to Spring container
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean  // This bean is for security filter chain configuration
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                // Any request must be authenticated with a user who has the role of 'ADMIN'
                .anyRequest().hasAnyRole("ADMIN", "POSTER", "RENTER")
            )
            // Use basic HTTP authentication for security
            .httpBasic(Customizer.withDefaults());
        
        // Build and return the filter chain
        return http.build();
    }

    @Bean  // This bean is for password encoding
    public BCryptPasswordEncoder passwordEncoder() {
        // Use BCrypt password encoder for hashing passwords
        return new BCryptPasswordEncoder();
    }
}

