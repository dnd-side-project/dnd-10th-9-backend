package com.dnd.dotchi.domain.card.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.dnd.dotchi.domain.card.dto.request.CardsByThemeRequest;
import com.dnd.dotchi.domain.card.dto.request.CardsWriteRequest;
import com.dnd.dotchi.domain.card.dto.response.CardsByThemeResponse;
import com.dnd.dotchi.domain.card.dto.response.CardsWriteResponse;
import com.dnd.dotchi.domain.card.dto.response.RecentCardsByThemeResponse;
import com.dnd.dotchi.domain.card.dto.response.WriteCommentOnCardResponse;
import com.dnd.dotchi.domain.card.dto.response.resultinfo.CardsRequestResultType;
import com.dnd.dotchi.domain.card.entity.Card;
import com.dnd.dotchi.domain.card.entity.TodayCard;
import com.dnd.dotchi.domain.card.entity.vo.CardSortType;
import com.dnd.dotchi.domain.card.exception.CardExceptionType;
import com.dnd.dotchi.domain.card.exception.ThemeExceptionType;
import com.dnd.dotchi.domain.card.repository.CardRepository;
import com.dnd.dotchi.domain.card.repository.TodayCardRepository;
import com.dnd.dotchi.domain.member.exception.MemberExceptionType;
import com.dnd.dotchi.global.exception.NotFoundException;
import com.dnd.dotchi.global.exception.RetryLimitExceededException;
import jakarta.persistence.EntityManager;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional
@SpringBootTest
class CardServiceTest {

    @Autowired
    CardService cardService;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    TodayCardRepository todayCardRepository;

    @Autowired
    EntityManager em;

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

    @Test
    @DisplayName("카드에 댓글을 작성한다.")
    void writeCommentOnCard() {
        // given
        // data.sql
        final long cardId = 2L;

        // when
        final Long oldCommentCount = cardRepository.findById(cardId).get().getCommentCount();
        final WriteCommentOnCardResponse result = cardService.writeCommentOnCard(cardId);
        em.flush();
        em.clear();

        // then
        final Card card = cardRepository.findById(cardId).get();
        final TodayCard todayCard = todayCardRepository.findByCardId(cardId).get();
        final CardsRequestResultType resultType = CardsRequestResultType.WRITE_COMMENT_ON_CARD_SUCCESS;

        assertSoftly(softly -> {
            softly.assertThat(card.getCommentCount()).isEqualTo(oldCommentCount + 1L);
            softly.assertThat(result.code()).isEqualTo(resultType.getCode());
            softly.assertThat(result.message()).isEqualTo(resultType.getMessage());
            softly.assertThat(todayCard.getTodayCommentCount()).isOne();
        });
    }

    @Test
    @DisplayName("카드에 댓글 작성시, 존재하지 않는 카드 ID를 전달할 때 NotFound 예외가 발생한다.")
    void writeCommentOnCardNotFoundException() {
        // given
        // data.sql
        final long cardId = cardRepository.count() + 1L;

        // when, then
        assertThatThrownBy(() -> cardService.writeCommentOnCard(cardId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(CardExceptionType.NOT_FOUND_CARD.getMessage());
    }

    @Test
    @DisplayName("여러 사용자가 동시에 같은 카드의 댓글을 작성할 경우, 동기화 처리가 정상 동작된다.")
    void handleConcurrentCommentsOnCardSynchronously() {
        // given
        // data.sql
        long cardId = 1L;

        CompletableFuture<Void> futureA = CompletableFuture.runAsync(() -> cardService.writeCommentOnCard(cardId));
        CompletableFuture<Void> futureB = CompletableFuture.runAsync(() -> cardService.writeCommentOnCard(cardId));

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(futureA, futureB);

        // when, then
        assertThatNoException().isThrownBy(combinedFuture::get);
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
        final BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "jpg", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
