package com.david.savetodouserservice.User;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toUser(CreateUserRequest userRequest) {
        return new User(userRequest.name(), userRequest.email(), userRequest.password());
    }

    public UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }

}
