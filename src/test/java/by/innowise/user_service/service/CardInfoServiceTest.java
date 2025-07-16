package by.innowise.user_service.service;

import by.innowise.user_service.dto.card.CardDto;
import by.innowise.user_service.dto.card.CardFilterDto;
import by.innowise.user_service.dto.card.CreateCardDto;
import by.innowise.user_service.dto.card.UpdateCardDto;
import by.innowise.user_service.entity.CardInfo;
import by.innowise.user_service.entity.User;
import by.innowise.user_service.exception.CardNotFoundException;
import by.innowise.user_service.exception.UserNotFoundException;
import by.innowise.user_service.mapper.CardInfoMapper;
import by.innowise.user_service.repository.CardInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CardInfoServiceTest {
    @Mock
    private CardInfoRepository cardInfoRepository;
    @Mock
    private UserService userService;

    private CardInfoMapper cardInfoMapper;

    private CardInfoService cardInfoService;

    @BeforeEach
    void setUp() {
        cardInfoMapper = Mappers.getMapper(CardInfoMapper.class);
        cardInfoService = new CardInfoService(cardInfoRepository, cardInfoMapper, userService);
    }

    @Test
    void testCreateCard_Success() {
        CreateCardDto createCardDto = new CreateCardDto();
        createCardDto.setHolder("John Doe");
        createCardDto.setUserId(1L);

        User user = new User();
        user.setId(1L);
        user.setCards(new ArrayList<>());

        CardInfo expected = new CardInfo();
        expected.setHolder("John Doe");
        expected.setUser(user);

        when(userService.getUserById(1L))
                .thenReturn(user);
        when(cardInfoRepository.save(any(CardInfo.class)))
                .thenReturn(expected);

        CardDto actual = cardInfoService.createCard(createCardDto);

        assertEquals(expected.getHolder(), actual.getHolder());
        assertEquals(1L, actual.getUserId());
    }

    @Test
    void testCreateCard_ShouldThrowException_WhenUserNotFound() {
        CreateCardDto createCardDto = new CreateCardDto();
        createCardDto.setHolder("John Doe");
        createCardDto.setUserId(1L);

        when(userService.getUserById(1L))
                .thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class,
                () -> cardInfoService.createCard(createCardDto));
    }

    @Test
    void testUpdateCard_Success() {
        LocalDate expirationDate = LocalDate.of(2027, 1, 1);
        UpdateCardDto updateCardDto = new UpdateCardDto();
        updateCardDto.setHolder("John Doe");
        updateCardDto.setExpirationDate(expirationDate);

        CardInfo card = new CardInfo();
        card.setId(1L);
        card.setHolder("John");
        card.setExpirationDate(LocalDate.of(2023, 1, 1));

        when(cardInfoRepository.findById(1L))
                .thenReturn(Optional.of(card));
        CardDto actual = cardInfoService.updateCard(1L, updateCardDto);

        assertEquals("John Doe", actual.getHolder());
        assertEquals(expirationDate, actual.getExpirationDate());
    }

    @Test
    void testUpdateCard_ShouldThrowException_WhenCardNotFound() {
        when(cardInfoRepository.findById(100L))
                .thenThrow(new CardNotFoundException("Card not found"));

        assertThrows(CardNotFoundException.class,
                () -> cardInfoService.updateCard(100L, new UpdateCardDto()));
    }

    @Test
    void testDeleteCard_Success() {
        when(cardInfoRepository.findById(1L))
                .thenReturn(Optional.of(new CardInfo()));
        cardInfoService.deleteCard(1L);

        verify(cardInfoRepository, times(1))
                .delete(any(CardInfo.class));
    }

    @Test
    void testDeleteUser_ShouldThrowException_WhenCardNotFound() {
        when(cardInfoRepository.findById(100L))
                .thenThrow(new CardNotFoundException("Card not found"));

        assertThrows(CardNotFoundException.class,
                () -> cardInfoService.deleteCard(100L));
    }

    @Test
    void getCardDtoById_Success() {
        LocalDate expirationDate = LocalDate.of(2027, 1, 1);

        CardInfo cardInfo = new CardInfo();
        cardInfo.setId(1L);
        cardInfo.setHolder("John Doe");
        cardInfo.setExpirationDate(expirationDate);

        when(cardInfoRepository.findById(1L))
                .thenReturn(Optional.of(cardInfo));

        CardDto actual = cardInfoService.getCardDtoById(1L);

        assertEquals(1L, actual.getId());
        assertEquals("John Doe", actual.getHolder());
        assertEquals(expirationDate, actual.getExpirationDate());
    }

    @Test
    void getCardDtoById_ShouldThrowException_WhenCardNotFound() {
        when(cardInfoRepository.findById(100L))
                .thenThrow(new CardNotFoundException("Card not found"));

        assertThrows(CardNotFoundException.class,
                () -> cardInfoService.getCardDtoById(100L));
    }

    @Test
    void testGetCardsWithFilters() {
        CardFilterDto filters = new CardFilterDto();
        filters.setHolder("Ann");

        Pageable pageable = PageRequest.of(0, 10);

        CardInfo card1 = new CardInfo();
        card1.setHolder("Ann Doe");
        CardInfo card2 = new CardInfo();
        card2.setHolder("Anna Glen");

        Page<CardInfo> cards = new PageImpl<>(List.of(card1, card2));

        when(cardInfoRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(cards);

        Page<CardDto> result = cardInfoService
                .getCardsWithFilters(filters, pageable);

        assertEquals(2, result.getContent().size());
    }

}
