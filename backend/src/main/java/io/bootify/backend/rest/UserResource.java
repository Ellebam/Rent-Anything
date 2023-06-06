package io.bootify.backend.rest;

import io.bootify.backend.model.UserDTO;
import io.bootify.backend.model.UserUpdateDTO;
import io.bootify.backend.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserResource {

    private final UserService userService;

    public UserResource(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id or hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> getUser(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(userService.get(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createUser(@RequestBody @Valid final UserDTO userDTO) {
        final Long createdId = userService.create(userDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> updateUser(@PathVariable(name = "id") final Long id,
                                           @RequestBody @Valid final UserUpdateDTO userUpdateDTO) {
        userService.update(id, userUpdateDTO);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id or hasRole('ROLE_ADMIN')")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") final Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
