package com.dnd.dotchi.domain.card.repository;

import com.dnd.dotchi.domain.card.entity.Card;
import com.dnd.dotchi.domain.card.entity.vo.CardSortType;
import java.util.List;

public interface CardCustomRepository {

    List<Card> findCardsByThemeWithFilteringAndPaging(
            Long themeId,
            CardSortType cardSortType,
            Long lastCardId,
            Long lastCardCommentCount
    );

    List<Card> findCardsAllWithFilteringAndPaging(
            CardSortType cardSortType,
            Long lastCardId,
            Long lastCardCommentCount
    );

}
