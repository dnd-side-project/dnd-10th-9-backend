package com.dnd.dotchi.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberModifyResponse(
	Integer code,

	String message
) {
}
