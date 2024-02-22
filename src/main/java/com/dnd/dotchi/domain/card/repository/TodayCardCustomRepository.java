package com.dnd.dotchi.domain.card.repository;

import java.util.List;

import com.dnd.dotchi.domain.card.entity.TodayCard;

public interface TodayCardCustomRepository {
	List<TodayCard> findTodayCards(final List<Long> idsRelatedToBlocking);
}
