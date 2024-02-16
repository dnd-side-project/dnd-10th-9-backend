package com.dnd.dotchi.domain.report.dto.response;

import com.dnd.dotchi.domain.report.dto.response.resultinfo.ReportRequestResultType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "신고 응답")
public record ReportResponse(
        @Schema(description = "응답 코드", example = "100")
        Integer code,

        @Schema(description = "응답 상태 메시지", example = "요청에 성공하였습니다.")
        String message
) {

    public static ReportResponse from(final ReportRequestResultType resultType) {
        return new ReportResponse(resultType.getCode(), resultType.getMessage());
    }

}
