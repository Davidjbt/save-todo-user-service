package com.david.savetodouserservice.User;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest userRequest) {
        UserResponse createdUser = userService.createUser(userRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdUser.id())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(createdUser);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable long id) {
        return ResponseEntity
                .ok(userService.findUserById(id));
    }

}
