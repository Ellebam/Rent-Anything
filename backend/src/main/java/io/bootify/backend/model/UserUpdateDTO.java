package io.bootify.backend.model;

import io.bootify.backend.domain.Role;

public class UserUpdateDTO {
    
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean canPostOffer;
    private Role role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getCanPostOffer() {
        return canPostOffer;
    }

    public void setCanPostOffer(Boolean canPostOffer) {
        this.canPostOffer = canPostOffer;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
