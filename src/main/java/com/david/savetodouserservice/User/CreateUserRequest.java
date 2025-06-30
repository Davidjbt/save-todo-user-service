package com.david.savetodouserservice.User;

public record CreateUserRequest(
        String name,
        String email,
        String password
) {
}
