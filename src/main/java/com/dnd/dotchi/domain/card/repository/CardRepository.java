package com.dnd.dotchi.domain.card.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dnd.dotchi.domain.card.entity.Card;

public interface CardRepository extends JpaRepository<Card, Long>, CardCustomRepository {
	List<Card> findTop5ByOrderByIdDesc();

}
