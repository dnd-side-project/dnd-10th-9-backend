package com.dnd.dotchi.domain.report.controller;

import com.dnd.dotchi.domain.report.dto.response.ReportResponse;
import com.dnd.dotchi.domain.report.request.ReportRequest;
import com.dnd.dotchi.global.exception.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "신고", description = "신고 API")
public interface ReportControllerDocs {

    @Operation(summary = "신고하기", description = "타 유저를 신고한다.")
    @ApiResponse(
            responseCode = "200",
            description = "신고 성공"
    )
    @ApiResponse(
            responseCode = "400",
            description = """
                    1.신고 이유가 50글자를 초과한 경우
                    2.신고 이유가 null인 경우
                    """,
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = """
                    1. 신고자 ID가 존재하지 않는 회원 ID인 경우
                    2. 신고 당한 유저 ID가 존재하지 않는 회원 ID인 경우
                    """,
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    ResponseEntity<ReportResponse> report(
            @Parameter(description = "신고 당한 유저 ID", example = "1") final Long reportedId,
            final ReportRequest request
    );

}
