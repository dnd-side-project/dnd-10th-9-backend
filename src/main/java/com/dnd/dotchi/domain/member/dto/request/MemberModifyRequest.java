package com.dnd.dotchi.domain.member.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record MemberModifyRequest(
	MultipartFile memberImage,

	String memberName,

	String memberDescription
) {
}
