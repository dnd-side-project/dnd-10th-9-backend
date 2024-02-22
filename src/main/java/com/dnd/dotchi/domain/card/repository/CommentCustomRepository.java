package com.dnd.dotchi.domain.card.repository;

import java.util.List;

import com.dnd.dotchi.domain.card.entity.Comment;

public interface CommentCustomRepository {
	List<Comment> findTop3LatestCommentsFilter(final List<Long> idsRelatedToBlocking, final Long cardId);
}
