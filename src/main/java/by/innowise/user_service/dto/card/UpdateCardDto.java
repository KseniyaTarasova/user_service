package by.innowise.user_service.dto.card;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateCardDto {
    @NotNull
    @Future(message = "Expiration date must be in the future")
    LocalDate expirationDate;
}
