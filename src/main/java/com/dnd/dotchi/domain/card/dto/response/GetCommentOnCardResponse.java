package com.dnd.dotchi.domain.card.dto.response;

import java.util.List;

import com.dnd.dotchi.domain.card.dto.response.resultinfo.CardsRequestResultType;
import com.dnd.dotchi.domain.card.entity.Card;
import com.dnd.dotchi.domain.card.entity.Comment;
import com.dnd.dotchi.domain.member.entity.Member;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "댓글 조회 응답")
public record GetCommentOnCardResponse(
        @Schema(description = "응답 코드", example = "1040")
        Integer code,

        @Schema(description = "응답 상태 메시지", example = "요청에 성공하였습니다.")
        String message,

        @Schema(description = "응답 결과")
        CommentResultResponse result
) {

        public static GetCommentOnCardResponse of(
            final Card card,
            final List<Member> authors,
            final CardsRequestResultType resultType
        ) {
                return new GetCommentOnCardResponse(
                    resultType.getCode(),
                    resultType.getMessage(),
                    parseCommentsResponse(card, authors)
                );
        }

        private static CommentResultResponse parseCommentsResponse(
            final Card card,
            final List<Member> authors
        ) {
                return new CommentResultResponse(
                    CardsResponse.from(card),
                    authors.stream()
                        .map(CommentsResponse::from)
                        .toList()
                );
        }

}
