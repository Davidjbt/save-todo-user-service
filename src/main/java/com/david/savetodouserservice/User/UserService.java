package com.david.savetodouserservice.User;

import java.util.List;

public interface UserService {

    UserResponse createUser(CreateUserRequest userRequest);

    UserResponse findUserById(Long id);

    List<UserResponse> findAllUsers();

    UserResponse updateUser(Long id, UpdateUserRequest userRequest);

    void deleteUser(Long id);
}
