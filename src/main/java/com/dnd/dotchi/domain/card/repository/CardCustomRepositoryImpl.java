package com.dnd.dotchi.domain.card.repository;

import static com.dnd.dotchi.domain.card.entity.QCard.card;
import static com.querydsl.jpa.JPAExpressions.select;

import com.dnd.dotchi.domain.card.entity.Card;
import com.dnd.dotchi.domain.card.entity.vo.CardSortType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CardCustomRepositoryImpl implements CardCustomRepository {

    private static final int BASIC_PAGE_SIZE = 10;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Card> findCardsByThemeWithFilteringAndPaging(
            final Long themeId,
            final CardSortType cardSortType,
            final Long lastCardId,
            final Long lastCardCommentCount
    ) {
        return jpaQueryFactory.selectFrom(card)
                .join(card.member).fetchJoin()
                .join(card.theme).fetchJoin()
                .where(
                        card.theme.id.eq(themeId),
                        defineCriteriaForSortedCards(cardSortType, lastCardId, lastCardCommentCount)
                )
                .orderBy(orderBy(cardSortType))
                .limit(BASIC_PAGE_SIZE)
                .fetch();
    }

    @Override
    public List<Card> findCardsAllWithFilteringAndPaging(
            final CardSortType cardSortType,
            final Long lastCardId,
            final Long lastCardCommentCount
    ) {
        return jpaQueryFactory.selectFrom(card)
                .join(card.member).fetchJoin()
                .join(card.theme).fetchJoin()
                .where(defineCriteriaForSortedCards(cardSortType, lastCardId, lastCardCommentCount))
                .orderBy(orderBy(cardSortType))
                .limit(BASIC_PAGE_SIZE)
                .fetch();
    }

    @Override
    public List<Card> findCardsByMemberWithFilteringAndPaging(final Long memberId, final Long lastCardId) {
        return jpaQueryFactory.selectFrom(card)
                .join(card.member).fetchJoin()
                .join(card.theme).fetchJoin()
                .where(
                        card.member.id.eq(memberId),
                        card.id.lt(lastCardId)
                )
                .orderBy(card.id.desc())
                .limit(BASIC_PAGE_SIZE)
                .fetch();
    }

    @Override
    public List<Card> findRecentCardByThemes() {
        return jpaQueryFactory
                .selectFrom(card)
                .where(card.id.in(
                        select(card.id.max())
                                .from(card)
                                .groupBy(card.theme.id)
                ))
                .orderBy(card.theme.id.asc())
                .fetch();
    }

    private Predicate defineCriteriaForSortedCards(
            final CardSortType cardSortType,
            final Long lastCardId,
            final Long lastCardCommentCount
    ) {
        return switch (cardSortType) {
            case LATEST -> card.id.lt(lastCardId);
            case HOT -> card.commentCount.lt(lastCardCommentCount);
        };
    }

    private OrderSpecifier<?> orderBy(final CardSortType cardSortType) {
        return switch (cardSortType) {
            case LATEST -> card.id.desc();
            case HOT -> card.commentCount.desc();
        };
    }

}
