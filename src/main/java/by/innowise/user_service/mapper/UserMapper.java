package by.innowise.user_service.mapper;

import by.innowise.user_service.dto.user.CreateUserDto;
import by.innowise.user_service.dto.user.UpdateUserDto;
import by.innowise.user_service.dto.user.UserDto;
import by.innowise.user_service.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(CreateUserDto dto);

    UserDto toDto(User entity);

    void update(UpdateUserDto dto, @MappingTarget User user);
}
