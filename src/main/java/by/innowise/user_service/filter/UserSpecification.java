package by.innowise.user_service.filter;

import by.innowise.user_service.dto.user.UserFilterDto;
import by.innowise.user_service.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {
    public static Specification<User> withFilter(UserFilterDto filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getName() != null && !filter.getName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
            }

            if (filter.getSurname() != null && !filter.getSurname().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("surname")), "%" + filter.getSurname().toLowerCase() + "%"));
            }

            if (filter.getEmail() != null && !filter.getEmail().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + filter.getEmail().toLowerCase() + "%"));
            }

            if (filter.getBirthDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("birthDate"), filter.getBirthDateFrom()));
            }

            if (filter.getBirthDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("birthDate"), filter.getBirthDateTo()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
