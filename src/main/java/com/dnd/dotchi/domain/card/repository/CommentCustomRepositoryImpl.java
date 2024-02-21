package com.dnd.dotchi.domain.card.repository;

import static com.dnd.dotchi.domain.blacklist.entity.QBlackList.*;
import static com.dnd.dotchi.domain.card.entity.QCard.*;
import static com.dnd.dotchi.domain.card.entity.QComment.*;
import static com.querydsl.jpa.JPAExpressions.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dnd.dotchi.domain.card.entity.Card;
import com.dnd.dotchi.domain.card.entity.Comment;
import com.dnd.dotchi.domain.card.entity.vo.CardSortType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class CommentCustomRepositoryImpl implements CommentCustomRepository {

    private static final int LIMIT_COMMENTS = 3;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Comment> findTop3LatestCommentsFilter(final Long viewerId, final Long cardId) {
        return blacklistFilter(viewerId)
            .where(
                comment.card.id.eq(cardId),
                blackList.blacklister.id.isNull(),
                blackList.blacklisted.id.isNull()
            )
            .orderBy(comment.id.desc())
            .limit(LIMIT_COMMENTS)
            .fetch();
    }

    private JPAQuery<Comment> blacklistFilter(final Long viewerId) {
        return jpaQueryFactory.selectFrom(comment)
            .join(comment.card).fetchJoin()
            .join(comment.member).fetchJoin()
            .leftJoin(blackList)
            .on(
                comment.member.id.eq(blackList.blacklisted.id)
                    .and(blackList.blacklister.id.eq(viewerId))
                .or(comment.member.id.eq(blackList.blacklister.id)
                    .and(blackList.blacklisted.id.eq(viewerId)))
            );
    }

}
