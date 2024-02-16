package com.dnd.dotchi.domain.card.repository;

import com.dnd.dotchi.domain.card.entity.Card;
import com.dnd.dotchi.domain.card.entity.vo.CardSortType;
import java.util.List;

public interface CardCustomRepository {

    List<Card> findCardsByThemeWithFilteringAndPaging(
            final Long themeId,
            final CardSortType cardSortType,
            final Long lastCardId,
            final Long lastCardCommentCount
    );

    List<Card> findCardsAllWithFilteringAndPaging(
            final CardSortType cardSortType,
            final Long lastCardId,
            final Long lastCardCommentCount
    );

    List<Card> findCardsByMemberWithFilteringAndPaging(
            final Long memberId,
            final Long lastCardId
    );

}
