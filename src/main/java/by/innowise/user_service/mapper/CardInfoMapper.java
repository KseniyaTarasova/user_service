package by.innowise.user_service.mapper;

import by.innowise.user_service.dto.card.CardDto;
import by.innowise.user_service.dto.card.CreateCardDto;
import by.innowise.user_service.dto.card.UpdateCardDto;
import by.innowise.user_service.entity.CardInfo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CardInfoMapper {
    CardInfo toEntity(CreateCardDto dto);

    CardDto toDto(CardInfo entity);

    void update(UpdateCardDto dto, @MappingTarget CardInfo card);
}
