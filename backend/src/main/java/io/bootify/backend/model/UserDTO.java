package io.bootify.backend.model;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import io.bootify.backend.domain.Role;

public class UserDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String username;

    @NotNull
    @Size(max = 255)
    private String password;

    @NotNull
    @Size(max = 255)
    private String email;

    @NotNull
    @Size(max = 255)
    private String firstName;

    @NotNull
    @Size(max = 255)
    private String lastName;

    @NotNull
    private Boolean canPostOffer;

    private Role role;



    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public Boolean getCanPostOffer() {
        return canPostOffer;
    }

    public void setCanPostOffer(final Boolean canPostOffer) {
        this.canPostOffer = canPostOffer;
    }

    public Role getRole() {
        return role;
    }
    
    public void setRole(final Role role) {
        this.role = role;
    }
}
