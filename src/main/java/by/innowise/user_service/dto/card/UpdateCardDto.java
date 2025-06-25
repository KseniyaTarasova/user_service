package by.innowise.user_service.dto.card;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
public class UpdateCardDto {
    @NotBlank(message = "Holder should not be empty")
    @Length(min = 2, max = 50, message = "Holder should be between 2 and 50 characters")
    private String holder;

    @NotNull
    @Future(message = "Expiration date must be in the future")
    LocalDate expirationDate;
}
