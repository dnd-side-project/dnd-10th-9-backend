package com.dnd.dotchi.domain.card.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dnd.dotchi.domain.card.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findTop3ByCardIdOrderByIdDesc(Long cardId);
}
