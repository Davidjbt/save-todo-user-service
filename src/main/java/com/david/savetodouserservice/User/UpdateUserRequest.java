package com.david.savetodouserservice.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(

        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 50, message = "Name must have between 2 and 50 characters")
        String name
) {
}
