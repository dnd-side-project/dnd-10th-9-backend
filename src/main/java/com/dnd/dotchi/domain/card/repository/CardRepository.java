package com.dnd.dotchi.domain.card.repository;

import com.dnd.dotchi.domain.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long>, CardCustomRepository {
}