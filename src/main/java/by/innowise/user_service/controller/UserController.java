package by.innowise.user_service.controller;

import by.innowise.user_service.dto.user.CreateUserDto;
import by.innowise.user_service.dto.user.UpdateUserDto;
import by.innowise.user_service.dto.user.UserDto;
import by.innowise.user_service.dto.user.UserFilterDto;
import by.innowise.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid CreateUserDto dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody @Valid UpdateUserDto dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(userService.getUserDtoById(id));
    }

    @GetMapping
    public ResponseEntity<Page<UserDto>> getUsersWithFilters(@ModelAttribute UserFilterDto dto,
                                                             @PageableDefault(sort = "id") Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(userService.getUsersWithFilters(dto, pageable));
    }
}
