package com.dnd.dotchi.domain.card.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dnd.dotchi.domain.card.entity.Card;

public interface CardRepository extends JpaRepository<Card, Long>, CardCustomRepository {
	List<Card> findTop5ByOrderByIdDesc();

	@Query(value = """
					SELECT * FROM 
					(SELECT *, ROW_NUMBER() OVER
					(PARTITION BY THEME_ID ORDER BY ID DESC) AS RANK FROM CARD) 
					WHERE RANK = 1
					ORDER BY THEME_ID
					""", nativeQuery = true)
	List<Card> findRecentCardByThemes();
}
