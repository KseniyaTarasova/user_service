package by.innowise.user_service.controller;

import by.innowise.user_service.config.AbstractPostgresIT;
import by.innowise.user_service.dto.user.CreateUserDto;
import by.innowise.user_service.dto.user.UpdateUserDto;
import by.innowise.user_service.entity.User;
import by.innowise.user_service.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT extends AbstractPostgresIT {
    private static final String BASE_URL = "/users";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;

    private Long userId;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        User user1 = User.builder()
                .name("Alice")
                .surname("Smith")
                .birthDate(LocalDate.of(1995, 4, 12))
                .email("alice@example.com")
                .build();
        User user2 = User.builder()
                .name("Bob")
                .surname("Johnson")
                .birthDate(LocalDate.of(1990, 8, 25))
                .email("bob@example.com")
                .build();
        User user3 = User.builder()
                .name("Charlie")
                .surname("Brown")
                .birthDate(LocalDate.of(1985, 2, 20))
                .email("charlie@example.com")
                .build();
        User user4 = User.builder()
                .name("Diana")
                .surname("White")
                .birthDate(LocalDate.of(2000, 1, 15))
                .email("diana@example.com")
                .build();

        userRepository.saveAll(List.of(user1, user2, user3, user4));
        userId = user1.getId();
    }

    @Test
    void testCreateUser() throws Exception {
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setName("John");
        createUserDto.setSurname("Doe");
        createUserDto.setBirthDate(LocalDate.of(1990, 1, 1));
        createUserDto.setEmail("johndoe@example.com");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.surname").value("Doe"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"))
                .andExpect(jsonPath("$.email").value("johndoe@example.com"));
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setName("Andy");
        updateUserDto.setSurname("Smith");
        updateUserDto.setBirthDate(LocalDate.of(1995, 4, 12));
        updateUserDto.setEmail("alice@example.com");

        mockMvc.perform(put(BASE_URL + "/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Andy"))
                .andExpect(jsonPath("$.surname").value("Smith"));
    }

    @Test
    void testUpdateUser_ShouldReturnNotFound_WhenUserNotFound() throws Exception {
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setName("Andy");
        updateUserDto.setSurname("Smith");
        updateUserDto.setBirthDate(LocalDate.of(1995, 4, 12));
        updateUserDto.setEmail("alice@example.com");

        mockMvc.perform(put(BASE_URL + "/{id}", 0L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{id}", userId))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetUserById() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", userId))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.surname").value("Smith"));
    }

    @Test
    void testGetUsersWithFilters() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .param("name", "Di"))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].name").value("Diana"));
    }
}
