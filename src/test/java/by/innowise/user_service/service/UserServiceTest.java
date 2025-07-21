package by.innowise.user_service.service;

import by.innowise.user_service.dto.user.CreateUserDto;
import by.innowise.user_service.dto.user.UpdateUserDto;
import by.innowise.user_service.dto.user.UserDto;
import by.innowise.user_service.dto.user.UserFilterDto;
import by.innowise.user_service.entity.User;
import by.innowise.user_service.exception.UserNotFoundException;
import by.innowise.user_service.mapper.UserMapper;
import by.innowise.user_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    private UserMapper userMapper;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);
        userService = new UserService(userRepository, userMapper);
    }

    @Test
    void testCreateUser_Success() {
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setName("John");

        User expected = new User();
        expected.setId(1L);
        expected.setName("John");

        when(userRepository.save(any(User.class)))
                .thenReturn(expected);
        UserDto actual = userService.createUser(createUserDto);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void testUpdateUser_Success() {
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setName("John");
        updateUserDto.setSurname("Doe");
        updateUserDto.setBirthDate(LocalDate.of(1990, 1, 1));
        updateUserDto.setEmail("johndoe@example.com");

        User user = new User();
        user.setId(1L);
        user.setName("JohN");
        user.setSurname("DoE");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setEmail("johndoe@example.com");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        UserDto actual = userService.updateUser(1L, updateUserDto);

        assertEquals("John", actual.getName());
        assertEquals("Doe", actual.getSurname());
    }

    @Test
    void testUpdateUser_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(100L))
                .thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class,
                () -> userService.updateUser(100L, new UpdateUserDto()));
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(new User()));
        userService.deleteUser(1L);

        verify(userRepository, times(1))
                .delete(any(User.class));
    }

    @Test
    void testDeleteUser_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(100L))
                .thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class,
                () -> userService.deleteUser(100L));
    }

    @Test
    void getUserDtoById_Success() {
        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setSurname("Doe");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        UserDto actual = userService.getUserDtoById(1L);

        assertEquals(1L, actual.getId());
        assertEquals("John", actual.getName());
        assertEquals("Doe", actual.getSurname());
    }

    @Test
    void getUserDtoById_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(100L))
                .thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class,
                () -> userService.getUserDtoById(100L));
    }

    @Test
    void testGetUsersWithFilters() {
        UserFilterDto filters = new UserFilterDto();
        filters.setName("Jo");

        Pageable pageable = PageRequest.of(0, 10);

        User user1 = new User();
        user1.setName("John");
        User user2 = new User();
        user2.setName("Jocombo");

        Page<User> users = new PageImpl<>(List.of(user1, user2));

        when(userRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(users);

        Page<UserDto> result = userService
                .getUsersWithFilters(filters, pageable);

        assertEquals(2, result.getContent().size());
    }
}