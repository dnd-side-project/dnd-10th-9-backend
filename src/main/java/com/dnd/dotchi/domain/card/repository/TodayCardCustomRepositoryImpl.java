package com.dnd.dotchi.domain.card.repository;

import static com.dnd.dotchi.domain.card.entity.QTodayCard.*;
import static com.dnd.dotchi.domain.member.entity.QMember.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dnd.dotchi.domain.card.entity.QTodayCard;
import com.dnd.dotchi.domain.card.entity.TodayCard;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class TodayCardCustomRepositoryImpl implements TodayCardCustomRepository{

	private static final int LIMIT_TODAY_CARDS = 3;

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<TodayCard> findTodayCards(final List<Long> idsRelatedToBlocking) {
		return jpaQueryFactory.selectFrom(todayCard)
			.join(todayCard.card).fetchJoin()
			.join(todayCard.card.member).fetchJoin()
			.where(todayCard.card.member.id.notIn(idsRelatedToBlocking))
			.orderBy(todayCard.todayCommentCount.desc())
			.limit(LIMIT_TODAY_CARDS)
			.fetch();
	}

}
