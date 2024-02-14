package com.dnd.dotchi.domain.card.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.assertj.core.api.SoftAssertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dnd.dotchi.domain.card.dto.request.CardsByThemeRequest;
import com.dnd.dotchi.domain.card.dto.request.CardsWriteRequest;
import com.dnd.dotchi.domain.card.dto.response.CardsByThemeResponse;
import com.dnd.dotchi.domain.card.dto.response.CardsWriteResponse;
import com.dnd.dotchi.domain.card.dto.response.RecentCardsByThemeResponse;
import com.dnd.dotchi.domain.card.entity.vo.CardSortType;
import com.dnd.dotchi.domain.card.exception.CardExceptionType;
import com.dnd.dotchi.domain.card.exception.ThemeExceptionType;
import com.dnd.dotchi.domain.member.exception.MemberExceptionType;
import com.dnd.dotchi.global.exception.NotFoundException;

@Transactional
@SpringBootTest
class CardServiceTest {

    @Autowired
    private CardService cardService;

    @Test
    @DisplayName("테마별 카드를 인기순으로 가져온다.")
    void getCardsByThemeWithHotSortType() {
        // given
        // data.sql
        final CardsByThemeRequest request = new CardsByThemeRequest(
            2L,
            CardSortType.HOT,
            20L,
            20L
        );

        // when
        final CardsByThemeResponse result = cardService.getCardsByTheme(request);

        // then
        final List<RecentCardsByThemeResponse> responses = result.result().recentCards();
        assertSoftly(softly -> {
            softly.assertThat(responses).hasSize(5);
            softly.assertThat(responses.get(0).cardId()).isEqualTo(14);
            softly.assertThat(responses.get(1).cardId()).isEqualTo(18);
            softly.assertThat(responses.get(2).cardId()).isEqualTo(22);
            softly.assertThat(responses.get(3).cardId()).isEqualTo(26);
            softly.assertThat(responses.get(4).cardId()).isEqualTo(30);
        });
    }

    @Test
    @DisplayName("테마별 카드를 최신순으로 가져온다.")
    void getCardsByThemeWithLatestSortType() {
        // given
        // data.sql
        final CardsByThemeRequest request = new CardsByThemeRequest(
            2L,
            CardSortType.LATEST,
            20L,
            20L
        );

        // when
        final CardsByThemeResponse result = cardService.getCardsByTheme(request);

        // then
        final List<RecentCardsByThemeResponse> responses = result.result().recentCards();
        assertSoftly(softly -> {
            softly.assertThat(responses).hasSize(5);
            softly.assertThat(responses.get(0).cardId()).isEqualTo(18);
            softly.assertThat(responses.get(1).cardId()).isEqualTo(14);
            softly.assertThat(responses.get(2).cardId()).isEqualTo(10);
            softly.assertThat(responses.get(3).cardId()).isEqualTo(6);
            softly.assertThat(responses.get(4).cardId()).isEqualTo(2);
        });
    }

    @Test
    @DisplayName("카드 작성에 대한 응답을 확인한다.")
    void writeResponseSuccess() {
        // given
        // data.sql
        final CardsWriteRequest request = new CardsWriteRequest(
            2L,
            2L,
            mockingMultipartFile("test.jpg"),
            "hihi",
            "happy",
            "good"
        );

        // when
        final CardsWriteResponse result = cardService.write(request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.code()).isEqualTo(1030);
            softly.assertThat(result.message()).isEqualTo("카드 작성에 성공하였습니다.");
        });
    }

    @Test
    @DisplayName("카드 작성 시 멤버 ID가 존재하지 않을 경우를 테스트한다.")
    void writeResponseNotFoundErrorMember() {
        // given
        // data.sql
        final CardsWriteRequest request = new CardsWriteRequest(
            3L,
            2L,
            mockingMultipartFile("test.jpg"),
            "hihi",
            "happy",
            "good"
        );

        // when
        final NotFoundException exception
            = assertThrows(NotFoundException.class, () -> cardService.write(request));

        // then
        assertEquals(MemberExceptionType.NOT_FOUND_MEMBER.getCode(), exception.getExceptionType().getCode());
        assertEquals(MemberExceptionType.NOT_FOUND_MEMBER.getMessage(), exception.getExceptionType().getMessage());
    }

    @Test
    @DisplayName("카드 작성 시 테마 ID가 존재하지 않을 경우를 테스트한다.")
    void writeResponseNotFoundErrorTheme() {
        // given
        // data.sql
        final CardsWriteRequest request = new CardsWriteRequest(
            2L,
            5L,
            mockingMultipartFile("test.jpg"),
            "hihi",
            "happy",
            "good"
        );

        // when
        final NotFoundException exception
            = assertThrows(NotFoundException.class, () -> cardService.write(request));

        // then
        assertEquals(ThemeExceptionType.NOT_FOUND_THEME.getCode(), exception.getExceptionType().getCode());
        assertEquals(ThemeExceptionType.NOT_FOUND_THEME.getMessage(), exception.getExceptionType().getMessage());
    }

    private MultipartFile mockingMultipartFile(String fileName) {
        return new MockMultipartFile(
            "images",
            fileName,
            MediaType.IMAGE_JPEG_VALUE,
            generateMockImage()
        );
    }

    private byte[] generateMockImage() {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "jpg", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
