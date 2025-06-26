package by.innowise.user_service.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
public class CreateUserDto {
    @NotBlank(message = "Name should not be empty")
    @Length(min = 2, max = 20, message = "Name should be between 2 and 20 characters")
    private String name;

    @NotBlank(message = "Surname should not be empty")
    @Length(min = 1, max = 30, message = "Surname should be between 1 and 30 characters")
    private String surname;

    @NotNull(message = "Birth date should not be empty")
    @Past(message = "Birth date should be in the past")
    private LocalDate birthDate;

    @NotBlank(message = "Email should not be empty")
    @Email(message = "Invalid email format")
    private String email;
}
