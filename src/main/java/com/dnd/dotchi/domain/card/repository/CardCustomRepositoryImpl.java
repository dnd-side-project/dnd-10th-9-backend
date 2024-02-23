package com.dnd.dotchi.domain.card.repository;

import static com.dnd.dotchi.domain.card.entity.QCard.card;
import static com.dnd.dotchi.domain.card.entity.QTodayCard.*;
import static com.dnd.dotchi.domain.member.entity.QMember.*;
import static com.querydsl.jpa.JPAExpressions.select;

import com.dnd.dotchi.domain.card.entity.Card;
import com.dnd.dotchi.domain.card.entity.TodayCard;
import com.dnd.dotchi.domain.card.entity.vo.CardSortType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CardCustomRepositoryImpl implements CardCustomRepository {

    private static final int BASIC_PAGE_SIZE = 10;
    private static final int LIMIT_CARDS = 5;
    private static final int DEFAULT_FIRST_LAST_CARD_ID = 99999;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Card> findCardsByThemeWithFilteringAndPaging(
            final Long themeId,
            final CardSortType cardSortType,
            final Long lastCardId,
            final Long lastCardCommentCount,
            final List<Long> idsRelatedToBlocking
    ) {
        return jpaQueryFactory.selectFrom(card)
                .join(card.member).fetchJoin()
                .join(card.theme).fetchJoin()
                .where(
                        card.theme.id.eq(themeId),
                        card.member.id.notIn(idsRelatedToBlocking),
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
            final Long lastCardCommentCount,
            final List<Long> idsRelatedToBlocking
    ) {
        return jpaQueryFactory.selectFrom(card)
                .join(card.member).fetchJoin()
                .join(card.theme).fetchJoin()
                .where(
                        card.member.id.notIn(idsRelatedToBlocking),
                        defineCriteriaForSortedCards(cardSortType, lastCardId, lastCardCommentCount)
                )
                .orderBy(orderBy(cardSortType))
                .limit(BASIC_PAGE_SIZE)
                .fetch();
    }

    private Predicate defineCriteriaForSortedCards(
            final CardSortType cardSortType,
            final Long lastCardId,
            final Long lastCardCommentCount
    ) {
        return switch (cardSortType) {
            case LATEST -> card.id.lt(lastCardId);
            case HOT -> {
                if (lastCardId == DEFAULT_FIRST_LAST_CARD_ID) {
                    yield card.commentCount.loe(lastCardCommentCount);
                }
                yield card.commentCount.eq(lastCardCommentCount)
                        .and(card.id.gt(lastCardId))
                        .or(card.commentCount.lt(lastCardCommentCount));
            }
        };
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

    @Override
    public List<Card> findTop5ByOrderByIdDesc(List<Long> idsRelatedToBlocking) {
        return jpaQueryFactory.selectFrom(card)
            .join(card.member).fetchJoin()
            .where(card.member.id.notIn(idsRelatedToBlocking))
            .orderBy(card.commentCount.desc())
            .limit(LIMIT_CARDS)
            .fetch();
    }

    private OrderSpecifier<?> orderBy(final CardSortType cardSortType) {
        return switch (cardSortType) {
            case LATEST -> card.id.desc();
            case HOT -> card.commentCount.desc();
        };
    }

}
