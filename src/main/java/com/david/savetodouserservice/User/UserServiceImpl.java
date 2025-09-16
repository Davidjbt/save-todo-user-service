package com.david.savetodouserservice.User;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userMapper = new UserMapper();
    }

    @Override
    public UserResponse createUser(CreateUserRequest userRequest) {
        Optional<User> existingUser = userRepository.findByEmail(userRequest.email());

        if (existingUser.isPresent())
            throw new UserAlreadyExistsException("User already exists");

        User user = userMapper.toUser(userRequest);
        user = userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse findUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id.toString())
        );

        return userMapper.toUserResponse(user);
    }

    @Override
    public List<UserResponse> findAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest userRequest) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id.toString())
        );

        user.setName(userRequest.name());
        user = userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id.toString())
        );

        userRepository.delete(user);
    }

    @Override
    public boolean doesEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }

}
