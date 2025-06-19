package by.innowise.user_service.dto.card;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateCardDto {
    @NotNull
    private Long userId;

    @NotNull
    @Future(message = "Expiration date must be in the future")
    private LocalDate expirationDate;
}
