package com.david.savetodouserservice.User;

import org.springframework.stereotype.Service;

import java.util.List;

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
        User user = userMapper.toUser(userRequest);
        user = userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse findUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);

        // todo: Throw error if not found and create Exception and Handler

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
        User user = userRepository.findById(id).orElse(null);

        if (user == null) return null; // todo: Replace later with exception handling

        user.setName(userRequest.name());
        user = userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) return; // todo: Replace later with exception handling

        userRepository.delete(user);
    }

}
