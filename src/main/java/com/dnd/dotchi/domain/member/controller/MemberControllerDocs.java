package com.dnd.dotchi.domain.member.controller;

import com.dnd.dotchi.domain.member.dto.request.MemberInfoRequest;
import com.dnd.dotchi.domain.member.dto.request.MemberModifyRequest;
import com.dnd.dotchi.domain.member.dto.response.MemberInfoResponse;
import com.dnd.dotchi.domain.member.dto.response.MemberModifyResponse;
import com.dnd.dotchi.global.exception.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;

@Tag(name = "회원", description = "회원 API")
public interface MemberControllerDocs {

    @Operation(summary = "회원 정보 조회", description = "회원 정보를 조회하는 기능")
    @ApiResponse(
            responseCode = "200",
            description = "회원 정보 조회 성공"
    )
    @ApiResponse(
            responseCode = "400",
            description = """
                    1. 마지막 조회 카드 ID가 빈 값일 경우
                    2. 마지막 조회 카드 ID가 양수가 아닌 경우
                    """,
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = """
                    1. 존재하지 않는 멤버 ID인 경우
                    """,
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    ResponseEntity<MemberInfoResponse> getMemberInfo(
            @Parameter(description = "회원 ID", example = "1") final Long memberId,
            final MemberInfoRequest request
    );

    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정하는 기능")
    @ApiResponse(
        responseCode = "200",
        description = "회원 정보 수정 성공"
    )
    @ApiResponse(
        responseCode = "400",
        description = """
                    1. 회원 프로필 사진이 이미지 파일이 아닌 경우
                    2. 변수타입이 올바르지 않은 경우
                    3. 변수값이 올바르지 않은 경우
                    """,
        content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    @ApiResponse(
        responseCode = "404",
        description = "존재하지 않는 멤버 ID인 경우",
        content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    public ResponseEntity<MemberModifyResponse> patchMemberInfo(
        @Valid @ModelAttribute final MemberModifyRequest request
    );
}
