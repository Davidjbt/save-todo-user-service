package com.david.savetodouserservice.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    private final UserMapper userMapper = new UserMapper();
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, userMapper);
    }

    @Test
    void shouldCreateUser() {
        // Arrange
        CreateUserRequest userRequest = new CreateUserRequest("John Doe", "john@gmail.com", "password");

        User savedUser = new User(userRequest.name(), userRequest.email(), userRequest.password());
        savedUser.setId(1L);

        when(userRepository.findByEmail(userRequest.email())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(savedUser);

        // Act
        UserResponse actual = userService.createUser(userRequest);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.id()).isEqualTo(1L);
        assertThat(actual.name()).isEqualTo(userRequest.name());
        assertThat(actual.email()).isEqualTo(userRequest.email());

        verify(userRepository).findByEmail(userRequest.email());
        verify(userRepository).save(any());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldThrowUserAlreadyExistsExceptionWhenUserExists() {
        // Arrange
        CreateUserRequest userRequest = new CreateUserRequest("John Doe", "john@gmail.com", "password");
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("John Doe");
        existingUser.setEmail("john@gmail.com");
        existingUser.setPassword("password");

        when(userRepository.findByEmail(userRequest.email())).thenReturn(Optional.of(existingUser));

        // Act
        Throwable thrown = catchThrowableOfType(
                UserAlreadyExistsException.class,
                () -> userService.createUser(userRequest)
        );

        // Assert
        assertThat(thrown)
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("User already exists");
        verify(userRepository).findByEmail(userRequest.email());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldReturnUserWhenFoundById() {
        // Arrange
        Long id = 1L;
        User existingUser = new User();
        existingUser.setId(id);
        existingUser.setName("John Doe");
        existingUser.setEmail("john@gmail.com");
        existingUser.setPassword("password");

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));

        // Act
        UserResponse actual = userService.findUserById(id);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.id()).isEqualTo(existingUser.getId());
        assertThat(actual.name()).isEqualTo(existingUser.getName());
        assertThat(actual.email()).isEqualTo(existingUser.getEmail());

        verify(userRepository).findById(id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenFindingNonExistentUser() {
        // Arrange
        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Throwable thrown = catchThrowableOfType(
                ResourceNotFoundException.class,
                () -> userService.findUserById(id)
        );

        // Assert
        assertThat(thrown)
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("User not found with the given input data id: '%s'", id));
        verify(userRepository).findById(id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldFindAllUsers() {
        // Arrange
        User existingUser1 = new User();
        existingUser1.setId(1L);
        existingUser1.setName("John Doe");
        existingUser1.setEmail("john@gmail.com");
        existingUser1.setPassword("password");

        User existingUser2 = new User();
        existingUser2.setId(2L);
        existingUser2.setName("Jane Doe");
        existingUser2.setEmail("jane@gmail.com");
        existingUser2.setPassword("password");

        List<User> savedUsers = List.of(existingUser1, existingUser2);

        UserResponse expectedUser1 = new UserResponse(
                existingUser1.getId(),
                existingUser1.getName(),
                existingUser1.getEmail()
        );
        UserResponse expectedUser2 = new UserResponse(
                existingUser2.getId(),
                existingUser2.getName(),
                existingUser2.getEmail()
        );

        when(userRepository.findAll()).thenReturn(savedUsers);

        // Act
        List<UserResponse> actual = userService.findAllUsers();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).hasSize(savedUsers.size());
        assertThat(actual).containsExactly(expectedUser1, expectedUser2);

        verify(userRepository).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldUpdateUser() {
        // Arrange
        Long id = 1L;
        User existingUser = new User();
        existingUser.setId(id);
        existingUser.setName("John");
        existingUser.setEmail("test@gmail.com");
        existingUser.setPassword("password");

        UpdateUserRequest userRequest = new UpdateUserRequest("John Doe");

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        // Act
        UserResponse actual = userService.updateUser(id, userRequest);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.id()).isEqualTo(id);
        assertThat(actual.name()).isEqualTo(userRequest.name());
        assertThat(actual.email()).isEqualTo(existingUser.getEmail());

        verify(userRepository).findById(id);
        verify(userRepository).save(existingUser);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUpdatingNonExistentUser() {
        // Arrange
        Long id = 1L;
        UpdateUserRequest userRequest = new UpdateUserRequest("John Doe");

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Throwable thrown = catchThrowableOfType(
                ResourceNotFoundException.class,
                () -> userService.updateUser(id, userRequest)
        );

        // Assert
        assertThat(thrown)
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found with the given input data id: '%s'", id);

        verify(userRepository).findById(id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldDeleteUser() {
        // Arrange
        Long id = 1L;

        User existingUser = new User();

        existingUser.setId(id);
        existingUser.setName("John Doe");
        existingUser.setEmail("john@gmail.com");

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));

        // Act
        userService.deleteUser(id);

        // Assert
        verify(userRepository).findById(id);
        verify(userRepository).delete(existingUser);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenDeletingNonExistentUser() {
        // Arrange
        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Throwable thrown = catchThrowableOfType(
                ResourceNotFoundException.class,
                () -> userService.deleteUser(id)
        );

        // Assert
        assertThat(thrown)
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found with the given input data id: '%s'", id);

        verify(userRepository).findById(id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldReturnTrueWhenExistentUserSearchedByEmail() {
        // Arrange
        String email = "john@gmail.com";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        // Act
        boolean actual = userService.doesEmailExist(email);

        // Assert
        assertThat(actual).isTrue();

        verify(userRepository).existsByEmail(email);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldReturnFalseWhenNonExistentUserSearchedByEmail() {
        // Arrange
        String email = "john@gmail.com";

        when(userRepository.existsByEmail(email)).thenReturn(false);

        // Act
        boolean expected = userService.doesEmailExist(email);

        // Assert
        assertThat(expected).isFalse();

        verify(userRepository).existsByEmail(email);
        verifyNoMoreInteractions(userRepository);
    }

}