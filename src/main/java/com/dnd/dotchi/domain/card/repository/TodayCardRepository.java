package com.dnd.dotchi.domain.card.repository;

import com.dnd.dotchi.domain.card.entity.TodayCard;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodayCardRepository extends JpaRepository<TodayCard, Long> {
    Optional<TodayCard> findByCardId(final Long cardId);
}
