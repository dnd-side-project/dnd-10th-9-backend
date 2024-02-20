package com.dnd.dotchi.domain.member.dto.response;

import com.dnd.dotchi.domain.member.dto.response.resultinfo.MemberRequestResultType;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "멤버 수정 페이지 응답")
public record MemberModifyResponse(
	@Schema(description = "응답 코드", example = "1120")
	Integer code,

	@Schema(description = "응답 상태 메시지", example = "회원 정보 수정에 성공하였습니다.")
	String message
) {
	public static MemberModifyResponse of(final MemberRequestResultType resultType) {
		return new MemberModifyResponse(
			resultType.getCode(),
			resultType.getMessage()
		);
	}
}
