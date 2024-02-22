package com.dnd.dotchi.domain.card.repository;

import java.util.List;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dnd.dotchi.domain.card.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentCustomRepository {
    Optional<Comment> findByMemberIdAndCardId(final Long memberId, final Long cardId);
}
