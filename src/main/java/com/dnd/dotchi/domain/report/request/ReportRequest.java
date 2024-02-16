package com.dnd.dotchi.domain.report.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "신고 요청")
public record ReportRequest(
        @Schema(description = "신고자 ID", example = "1")
        Long reporterId,

        @Schema(description = "신고 이유", example = "이상한 글을 씁니다.")
        @NotBlank(message = "신고 이유는 빈 값일 수 없습니다.")
        @Size(max = 50, message = "신고 이유는 50자를 넘을 수 없습니다.")
        String reason
) {
}
