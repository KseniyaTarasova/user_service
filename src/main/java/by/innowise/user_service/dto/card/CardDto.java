package by.innowise.user_service.dto.card;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CardDto {
    private Long id;
    private Long userId;
    private String number;
    private String holder;
    private LocalDate expirationDate;
}
