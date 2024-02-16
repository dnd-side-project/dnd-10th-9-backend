package com.dnd.dotchi.domain.member.dto.response;

import com.dnd.dotchi.domain.member.dto.response.resultinfo.MemberRequestResultType;
import com.dnd.dotchi.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 인가 정보 응답")
public record MemberAuthorizationResponse(
        @Schema(description = "응답 코드", example = "1010")
        Integer code,

        @Schema(description = "응답 상태 메시지", example = "회원 정보 요청에 성공하였습니다.")
        String message,

        @Schema(description = "회원 인가 정보 결과")
        MemberAuthorizationResultResponse result
) {

    public static MemberAuthorizationResponse of(
            final MemberRequestResultType resultType,
            final Member member,
            final String accessToken
    ) {
        return new MemberAuthorizationResponse(
                resultType.getCode(),
                resultType.getMessage(),
                MemberAuthorizationResultResponse.of(member, accessToken)
        );
    }

}
