package com.dnd.dotchi.domain.card.dto.response;

import com.dnd.dotchi.domain.card.entity.Card;
import com.dnd.dotchi.domain.member.entity.Member;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카드 상세 정보 및 댓글 페이지에 대한 응답")
public record CommentsResponse(

	@Schema(description = "댓글 작성한 회원 ID")
	Long memberId,

	@Schema(description = "댓글 작성한 회원 이름")
	String memberName,

	@Schema(description = "댓글 작성한 회원 프로필 사진")
	String memberImageUrl
){

	public static CommentsResponse from(final Member member) {
		return new CommentsResponse(
			member.getId(),
			member.getNickname(),
			member.getImageUrl()
		);
	}

}
