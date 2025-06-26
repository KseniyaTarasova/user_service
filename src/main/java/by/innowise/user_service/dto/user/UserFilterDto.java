package by.innowise.user_service.dto.user;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserFilterDto {
    private String name;
    private String surname;
    private LocalDate birthDateFrom;
    private LocalDate birthDateTo;
    private String email;
}
