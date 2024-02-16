package com.dnd.dotchi.domain.blacklist.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "차단하기 요청")
public record BlockRequest(
        @Schema(description = "blacklister ID", example = "1")
        @NotNull(message = "blacklister ID는 빈 값일 수 없습니다.")
        @Positive(message = "blacklister ID는 양수만 가능합니다.")
        Long blacklisterId
) {
}
