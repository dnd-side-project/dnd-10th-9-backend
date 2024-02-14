package com.dnd.dotchi.domain.card.controller;

import com.dnd.dotchi.domain.card.dto.request.CardsByThemeRequest;
import com.dnd.dotchi.domain.card.dto.response.CardsByThemeResponse;
import com.dnd.dotchi.domain.card.dto.response.WriteCommentOnCardResponse;
import com.dnd.dotchi.global.exception.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "카드", description = "카드 API")
public interface CardControllerDocs {

    @Operation(summary = "테마별 카드 조회", description = "테마별 카드를 조회한다.")
    @ApiResponse(
            responseCode = "200",
            description = "테마별 카드 조회 성공"
    )
    @ApiResponse(
            responseCode = "400",
            description = """
                    1.Request의 변수 타입이 맞지 않는 경우
                    2.Request의 변수 값이 올바르지 않는 경우
                    """,
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 테마 ID인 경우",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    ResponseEntity<CardsByThemeResponse> getCardsByTheme(final CardsByThemeRequest request);

    @Operation(summary = "댓글 작성", description = "카드에 댓글을 작성한다.")
    @ApiResponse(
            responseCode = "200",
            description = "댓글 작성 성공"
    )
    @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 카드 ID인 경우",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    ResponseEntity<WriteCommentOnCardResponse> writeCommentOnCard(@PathVariable("cardId") Long cardId);

}
