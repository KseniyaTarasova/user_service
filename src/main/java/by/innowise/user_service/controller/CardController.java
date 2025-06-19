package by.innowise.user_service.controller;

import by.innowise.user_service.dto.card.CardDto;
import by.innowise.user_service.dto.card.CardFilterDto;
import by.innowise.user_service.dto.card.CreateCardDto;
import by.innowise.user_service.dto.card.UpdateCardDto;
import by.innowise.user_service.service.CardInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cards")
public class CardController {
    private final CardInfoService cardInfoService;

    @PostMapping
    public ResponseEntity<CardDto> createCard(@RequestBody @Valid CreateCardDto dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cardInfoService.createCard(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardDto> updateCard(@PathVariable Long id, @RequestBody @Valid UpdateCardDto dto) {
        return ResponseEntity.ok(cardInfoService.updateCard(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        cardInfoService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardDto> getCardById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(cardInfoService.getCardDtoById(id));
    }

    @GetMapping
    public ResponseEntity<Page<CardDto>> getCardsWithFilters(@ModelAttribute CardFilterDto dto,
                                                             @PageableDefault(sort = "id") Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(cardInfoService.getCardsWithFilters(dto, pageable));
    }
}
