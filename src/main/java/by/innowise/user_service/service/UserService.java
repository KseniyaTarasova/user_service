package by.innowise.user_service.service;

import by.innowise.user_service.dto.user.CreateUserDto;
import by.innowise.user_service.dto.user.UpdateUserDto;
import by.innowise.user_service.dto.user.UserDto;
import by.innowise.user_service.dto.user.UserFilterDto;
import by.innowise.user_service.entity.User;
import by.innowise.user_service.exception.UserNotFoundException;
import by.innowise.user_service.mapper.UserMapper;
import by.innowise.user_service.repository.UserRepository;
import by.innowise.user_service.filter.UserSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto createUser(CreateUserDto dto) {
        User user = userMapper.toEntity(dto);
        User savedUser = userRepository.save(user);

        log.info("User created: {}", savedUser);

        return userMapper.toDto(savedUser);
    }


    @Transactional
    @CachePut(value = "user", key = "#id")
    public UserDto updateUser(Long id, UpdateUserDto dto) {
        User user = getUserOrThrow(id);

        log.info("User before update: {}", user);

        userMapper.update(dto, user);

        log.info("User after update: {}", user);

        return userMapper.toDto(user);
    }

    @Transactional
    @CacheEvict(value = "user", key = "#id")
    public void deleteUser(Long id) {
        User user = getUserOrThrow(id);

        log.info("Deleted user: {}", user);

        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "user", key = "#id")
    public UserDto getUserDtoById(Long id) {
        User user = getUserOrThrow(id);

        log.info("User found: {}", user);

        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public Page<UserDto> getUsersWithFilters(UserFilterDto dto, Pageable pageable) {
        return userRepository.findAll(UserSpecification.withFilter(dto), pageable)
                .map(userMapper::toDto);
    }

    public User getUserById(Long id) {
        return getUserOrThrow(id);
    }

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }
}
