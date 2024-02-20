package com.dnd.dotchi.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "회원 정보 조회 요청")
public record MemberInfoRequest(
        @Schema(description = "마지막 조회 카드 ID", example = "99999999")
        @NotNull(message = "마지막 조회 카드 ID는 빈 값일 수 없습니다.")
        @Positive(message = "마지막 조회 카드 ID는 양수만 가능합니다.")
        Long lastCardId
) {
}
