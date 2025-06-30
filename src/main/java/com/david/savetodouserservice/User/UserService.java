package com.david.savetodouserservice.User;

public interface UserService {

    UserResponse createUser(CreateUserRequest userRequest);

    UserResponse findUserById(Long id);
}
