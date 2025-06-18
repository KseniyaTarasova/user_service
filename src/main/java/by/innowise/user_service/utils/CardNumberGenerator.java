package by.innowise.user_service.utils;

import by.innowise.user_service.repository.CardInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class CardNumberGenerator {
    private static final int CARD_NUMBER_LENGTH = 16;

    private final CardInfoRepository cardRepository;

    public String generateCardNumber() {
        String cardNumber;

        do {
            cardNumber = generate16Digits();
        } while (cardRepository.existsByNumber(cardNumber));

        return cardNumber;
    }

    private String generate16Digits() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(CARD_NUMBER_LENGTH);

        for (int i = 0; i < CARD_NUMBER_LENGTH; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }
}
