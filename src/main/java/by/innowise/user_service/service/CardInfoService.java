package by.innowise.user_service.service;

import by.innowise.user_service.dto.card.CardDto;
import by.innowise.user_service.dto.card.CardFilterDto;
import by.innowise.user_service.dto.card.CreateCardDto;
import by.innowise.user_service.dto.card.UpdateCardDto;
import by.innowise.user_service.entity.CardInfo;
import by.innowise.user_service.entity.User;
import by.innowise.user_service.exception.CardNotFoundException;
import by.innowise.user_service.filter.CardSpecification;
import by.innowise.user_service.mapper.CardInfoMapper;
import by.innowise.user_service.repository.CardInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardInfoService {
    private final CardInfoRepository cardRepository;
    private final CardInfoMapper cardMapper;
    private final UserService userService;

    @Transactional
    public CardDto createCard(CreateCardDto dto) {
        CardInfo card = cardMapper.toEntity(dto);
        User user = userService.getUserById(dto.getUserId());

        card.setUser(user);
        user.getCards().add(card);

        CardInfo savedCard = cardRepository.save(card);

        log.info("Card created: {}", savedCard);

        return cardMapper.toDto(savedCard);
    }

    @Transactional
    public CardDto updateCard(Long id, UpdateCardDto dto) {
        CardInfo card = getCardOrThrow(id);

        log.info("Card before update: {}", card);

        cardMapper.update(dto, card);

        log.info("Card after update: {}", card);

        return cardMapper.toDto(card);
    }

    @Transactional
    public void deleteCard(Long id) {
        CardInfo card = getCardOrThrow(id);

        log.info("Deleted card: {}", card);

        cardRepository.delete(card);
    }

    @Transactional(readOnly = true)
    public CardDto getCardDtoById(Long id) {
        CardInfo card = getCardOrThrow(id);

        log.info("Card found: {}", card);

        return cardMapper.toDto(card);
    }

    @Transactional(readOnly = true)
    public Page<CardDto> getCardsWithFilters(CardFilterDto dto, Pageable pageable) {
        return cardRepository.findAll(CardSpecification.withFilter(dto), pageable)
                .map(cardMapper::toDto);
    }

    private CardInfo getCardOrThrow(Long id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException("Card with id " + id + " not found"));
    }
}
