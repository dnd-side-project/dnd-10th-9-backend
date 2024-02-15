package com.dnd.dotchi.domain.card.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dnd.dotchi.domain.card.entity.Card;

public interface CardRepository extends JpaRepository<Card, Long>, CardCustomRepository {
}
