package by.innowise.user_service.dto.user;

import by.innowise.user_service.dto.card.CardDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String email;
    private List<CardDto> cards = new ArrayList<>();
}
