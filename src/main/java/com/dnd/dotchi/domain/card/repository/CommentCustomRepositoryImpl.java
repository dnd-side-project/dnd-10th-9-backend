package com.dnd.dotchi.domain.card.repository;

import static com.dnd.dotchi.domain.card.entity.QComment.comment;

import com.dnd.dotchi.domain.card.entity.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CommentCustomRepositoryImpl implements CommentCustomRepository {

    private static final int LIMIT_COMMENTS = 3;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Comment> findTop3LatestCommentsFilter(final List<Long> idsRelatedToBlocking, final Long cardId) {
        return jpaQueryFactory.selectFrom(comment)
                .join(comment.card).fetchJoin()
                .join(comment.member).fetchJoin()
                .where(
                        comment.member.id.notIn(idsRelatedToBlocking),
                        comment.card.id.eq(cardId)
                )
                .orderBy(comment.id.desc())
                .limit(LIMIT_COMMENTS)
                .fetch();
    }

}
