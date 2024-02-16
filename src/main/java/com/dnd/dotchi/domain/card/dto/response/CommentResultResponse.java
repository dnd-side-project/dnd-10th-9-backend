package com.dnd.dotchi.domain.card.dto.response;

import java.util.List;

import com.dnd.dotchi.domain.card.entity.Card;

import io.swagger.v3.oas.annotations.media.Schema;

public record CommentResultResponse(
	@Schema(description = "카드 상세 정보")
	CardsResponse card,

	@Schema(description = "댓글을 작성한 멤버 정보")
	List<CommentsResponse> comments
) {
}
