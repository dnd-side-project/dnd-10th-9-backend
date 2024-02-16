package com.dnd.dotchi.domain.blacklist.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "차단하기 응답")
public record BlockResponse(
        @Schema(description = "응답 코드", example = "100")
        Integer code,

        @Schema(description = "응답 상태 메시지", example = "요청에 성공하였습니다.")
        String message
) {
}
