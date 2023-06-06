package io.bootify.backend.config;

import io.bootify.backend.domain.Role;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "users")
public class PreconfiguredUsers {

    private final List<PreconfiguredUserProperties> initial;

    public PreconfiguredUsers(List<PreconfiguredUserProperties> initial) {
        this.initial = initial;
    }

    public List<PreconfiguredUserProperties> getInitial() {
        return initial;
    }

    public static class PreconfiguredUserProperties {

        private final String username;
        private final String password;
        private final String email;
        private final String firstName;
        private final String lastName;
        private final Boolean canPostOffer;
        private final Role role;

        public PreconfiguredUserProperties(String username, String password, String email,
                                           String firstName, String lastName, Boolean canPostOffer, Role role) {
            this.username = username;
            this.password = password;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.canPostOffer = canPostOffer;
            this.role = role;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getEmail() {
            return email;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public Boolean getCanPostOffer() {
            return canPostOffer;
        }

        public Role getRole() {
            return role;
        }
    }
}
