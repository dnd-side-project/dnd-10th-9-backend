package com.dnd.dotchi.domain.blacklist.controller;

import com.dnd.dotchi.domain.blacklist.dto.response.BlockResponse;
import com.dnd.dotchi.domain.member.entity.Member;
import com.dnd.dotchi.global.exception.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "블랙리스트", description = "블랙리스트 API")
public interface BlacklistControllerDocs {

    @Operation(summary = "차단하기", description = "타 유저를 차단한다.")
    @ApiResponse(
            responseCode = "200",
            description = "차단 성공"
    )
    @ApiResponse(
            responseCode = "404",
            description = """
                    1. blacklister 유저 ID가 존재하지 않는 회원 ID인 경우
                    2. blacklisted 유저 ID가 존재하지 않는 회원 ID인 경우
                    """,
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    ResponseEntity<BlockResponse> block(
            final Member member,
            @Parameter(description = "차단 당한 유저 ID", example = "1") final Long blacklistedId
    );

}
