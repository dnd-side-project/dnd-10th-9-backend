package com.dnd.dotchi.domain.blacklist.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "차단하기 요청")
public record BlockRequest(
        @Schema(description = "blacklister ID", example = "1")
        Long blacklisterId
) {
}
