package com.dnd.dotchi.domain.card.dto.response;

import java.util.List;

import com.dnd.dotchi.domain.card.entity.Card;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카드 정보 및 댓글에 대한 응답")
public record CommentResultResponse(
	@Schema(description = "카드 상세 정보")
	CardsResponse card,

	@Schema(description = "댓글을 작성한 멤버 정보")
	List<CommentsResponse> comments,

	@Schema(description = "댓글을 작성 여부에 대한 정보")
	Boolean hasComment
) {
}
