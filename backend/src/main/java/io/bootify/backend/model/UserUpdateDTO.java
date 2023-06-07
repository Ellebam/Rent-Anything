package io.bootify.backend.model;

import io.bootify.backend.domain.Role;

/**
 * Data Transfer Object used for updating user information.
 * Includes only those fields that a user is allowed to update.
 */
public class UserUpdateDTO {
    
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean canPostOffer;
    private Role role;

    /**
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the email of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the first name of the user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the last name of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the canPostOffer flag of the user
     */
    public Boolean getCanPostOffer() {
        return canPostOffer;
    }

    /**
     * @param canPostOffer the canPostOffer flag to set
     */
    public void setCanPostOffer(Boolean canPostOffer) {
        this.canPostOffer = canPostOffer;
    }

    /**
     * @return the role of the user
     */
    public Role getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(Role role) {
        this.role = role;
    }
}
