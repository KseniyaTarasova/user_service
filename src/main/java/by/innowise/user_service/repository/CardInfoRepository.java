package by.innowise.user_service.repository;

import by.innowise.user_service.entity.CardInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CardInfoRepository extends JpaRepository<CardInfo, Long>,
        JpaSpecificationExecutor<CardInfo> {
    boolean existsByNumber(String number);
}
