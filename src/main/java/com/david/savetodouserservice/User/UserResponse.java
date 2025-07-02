package com.david.savetodouserservice.User;

public record UserResponse(
        Long id,
        String name,
        String email
) {
}
