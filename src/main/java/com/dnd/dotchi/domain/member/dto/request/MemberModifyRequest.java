package com.dnd.dotchi.domain.member.dto.request;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "멤버 수정시 요청 데이터")
public record MemberModifyRequest(
	@Schema(description = "임시적인 멤버 ID", example = "1")
	Long id,

	@Schema(description = "반려생물 이미지", example = "image.jpg")
	MultipartFile memberImage,

	@Schema(description = "반려생물 이름", example = "오뜨")
	@NotBlank(message = "반려생물 이름은 빈 값일 수 없습니다.")
	@Size(max = 5, message = "반려생물 이름은 5자를 넘을 수 없습니다.")
	String memberName,

	@Schema(description = "반려동물 소개", example = "오뜨 최고")
	@Size(max = 40, message = "반려동물 소개는 40자를 넘을 수 없습니다.")
	String memberDescription
) {
}
