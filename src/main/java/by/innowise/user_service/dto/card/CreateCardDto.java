package by.innowise.user_service.dto.card;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
public class CreateCardDto {
    @NotNull(message = "User id should not be null")
    private Long userId;

    @NotBlank(message = "Holder should not be empty")
    @Length(min = 2, max = 50, message = "Holder should be between 2 and 50 characters")
    private String holder;

    @NotBlank(message = "Number should not be empty")
    @Pattern(regexp = "^[0-9]{16}$", message = "Card number must be 16 digits")
    private String number;

    @NotNull(message = "Expiration date should not be null")
    @Future(message = "Expiration date must be in the future")
    private LocalDate expirationDate;
}
