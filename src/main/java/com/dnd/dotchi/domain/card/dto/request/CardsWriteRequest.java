package com.dnd.dotchi.domain.card.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "카드 작성시 요청")
public record CardsWriteRequest(
        @Schema(description = "테마 ID", example = "1")
        @NotNull(message = "테마 ID는 빈 값일 수 없습니다.")
        @Positive(message = "테마 ID는 양수만 가능합니다.")
        Long themeId,

        @Schema(description = "카드 이미지", example = "image.jpg")
        @NotNull(message = "카드 이미지는 빈 값일 수 없습니다.")
        MultipartFile image,

        @Schema(description = "따봉네임", example = "따봉냥이")
        @NotBlank(message = "따봉네임은 빈 값일 수 없습니다.")
        @Size(max = 7, message = "따봉네임은 7자를 넘을 수 없습니다.")
        String backName,

        @Schema(description = "오늘의 기분", example = "엄지가 절로 올라가")
        @NotBlank(message = "오늘의 기분은 빈 값일 수 없습니다.")
        @Size(max = 15, message = "오늘의 기분은 15자를 넘을 수 없습니다.")
        String backMood,

        @Schema(description = "따봉 디테일", example = "이럴수가 넌 따봉냥이와 눈이 마주쳤어")
        @NotNull(message = "따봉 디테일은 null값 일 수 없습니다.")
        @Size(max = 20, message = "따봉 디테일은 20자를 넘을 수 없습니다.")
        String backContent
) {
}
