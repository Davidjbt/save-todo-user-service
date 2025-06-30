package com.david.savetodouserservice.User;

import org.springframework.stereotype.Service;

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

}
