package com.dnd.dotchi.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "회원 인가 정보 요청")
public record MemberAuthorizationRequest(
        @Schema(description = "멤버 ID", example = "1")
        @NotNull(message = "멤버 ID는 빈 값일 수 없습니다.")
        @Positive(message = "멤버 ID는 양수만 가능합니다.")
        Long memberId
) {
}
