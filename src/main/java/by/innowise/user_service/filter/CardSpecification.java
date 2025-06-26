package by.innowise.user_service.filter;

import by.innowise.user_service.dto.card.CardFilterDto;
import by.innowise.user_service.entity.CardInfo;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class CardSpecification {

    public static Specification<CardInfo> withFilter(CardFilterDto filter) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (filter.getHolder() != null && !filter.getHolder().isBlank()) {
                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("holder")), "%" + filter.getHolder().toLowerCase() + "%"));
            }

            if (filter.getNumber() != null && !filter.getNumber().isBlank()) {
                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("number")), "%" + filter.getNumber().toLowerCase() + "%"));
            }

            return predicate;
        };
    }
}