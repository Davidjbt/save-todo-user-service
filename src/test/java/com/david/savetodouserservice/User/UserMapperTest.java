package com.david.savetodouserservice.User;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    void shouldMapCreateUserRequestToUser() {
        // Arrange
        CreateUserRequest userRequest = new CreateUserRequest("John Doe", "john@gmail.com", "password");

        // Act
        User actual = userMapper.toUser(userRequest);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNull();
        assertThat(actual.getName()).isEqualTo(userRequest.name());
        assertThat(actual.getEmail()).isEqualTo(userRequest.email());
        assertThat(actual.getPassword()).isEqualTo(userRequest.password());
    }

    @Test
    void shouldMapUserToUserResponse() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@gmail.com");
        user.setPassword("password");

        // Act
        UserResponse actual = userMapper.toUserResponse(user);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.id()).isEqualTo(user.getId());
        assertThat(actual.name()).isEqualTo(user.getName());
        assertThat(actual.email()).isEqualTo(user.getEmail());
    }

}