package com.dnd.dotchi.domain.card.service;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.amazonaws.services.s3.AmazonS3;
import com.dnd.dotchi.domain.card.dto.request.CardsAllRequest;
import com.dnd.dotchi.domain.card.dto.request.CardsByThemeRequest;
import com.dnd.dotchi.domain.card.dto.request.CardsWriteRequest;
import com.dnd.dotchi.domain.card.dto.response.CardsAllResponse;
import com.dnd.dotchi.domain.card.dto.response.CardsByThemeResponse;
import com.dnd.dotchi.domain.card.dto.response.CardsResponse;
import com.dnd.dotchi.domain.card.dto.response.CardsWriteResponse;
import com.dnd.dotchi.domain.card.dto.response.DeleteCardResponse;
import com.dnd.dotchi.domain.card.dto.response.GetCommentOnCardResponse;
import com.dnd.dotchi.domain.card.dto.response.HomePageResponse;
import com.dnd.dotchi.domain.card.dto.response.RecentCardsByThemeResponse;
import com.dnd.dotchi.domain.card.dto.response.TodayCardsOnHomeResponse;
import com.dnd.dotchi.domain.card.dto.response.WriteCommentOnCardResponse;
import com.dnd.dotchi.domain.card.dto.response.resultinfo.CardsRequestResultType;
import com.dnd.dotchi.domain.card.entity.Card;
import com.dnd.dotchi.domain.card.entity.TodayCard;
import com.dnd.dotchi.domain.card.entity.vo.CardSortType;
import com.dnd.dotchi.domain.card.exception.CardExceptionType;
import com.dnd.dotchi.domain.card.exception.ThemeExceptionType;
import com.dnd.dotchi.domain.card.repository.CardRepository;
import com.dnd.dotchi.domain.card.repository.TodayCardRepository;
import com.dnd.dotchi.domain.member.entity.Member;
import com.dnd.dotchi.domain.member.service.MemberService;
import com.dnd.dotchi.global.exception.BadRequestException;
import com.dnd.dotchi.global.exception.NotFoundException;
import com.dnd.dotchi.global.redis.CacheMember;
import jakarta.persistence.EntityManager;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
    MemberService memberService;

    @Autowired
    TodayCardRepository todayCardRepository;

    @MockBean
    private AmazonS3 amazonS3Mock;

    @Autowired
    EntityManager em;

    @BeforeEach
    void setUp() throws MalformedURLException, URISyntaxException {
        URI uri = new URI("http://example.com/uploaded/image.jpg");
        given(amazonS3Mock.getUrl(anyString(), anyString())).willReturn(uri.toURL());
    }

    @Test
    @DisplayName("테마별 카드를 인기순으로 가져온다.")
    void getCardsByThemeWithHotSortType() {
        // given
        // data-test.sql
        final CardsByThemeRequest request = new CardsByThemeRequest(
                2L,
                CardSortType.HOT,
                20L,
                20L
        );
        final Member member = memberService.findById(1L);

        // when

        final CardsByThemeResponse result = cardService.getCardsByTheme(CacheMember.from(member), request);

        // then
        final List<RecentCardsByThemeResponse> responses = result.result().cards();
        assertSoftly(softly -> {
            softly.assertThat(responses).hasSize(1);
            softly.assertThat(responses.get(0).cardId()).isEqualTo(22);
        });
    }

    @Test
    @DisplayName("테마별 카드를 최신순으로 가져온다.")
    void getCardsByThemeWithLatestSortType() {
        // given
        // data-test.sql
        final CardsByThemeRequest request = new CardsByThemeRequest(
                2L,
                CardSortType.LATEST,
                20L,
                20L
        );
        final Member member = memberService.findById(1L);

        // when
        final CardsByThemeResponse result = cardService.getCardsByTheme(CacheMember.from(member), request);

        // then
        final List<RecentCardsByThemeResponse> responses = result.result().cards();
        assertSoftly(softly -> {
            softly.assertThat(responses).hasSize(2);
            softly.assertThat(responses.get(0).cardId()).isEqualTo(10);
            softly.assertThat(responses.get(1).cardId()).isEqualTo(6);
        });
    }

    @Test
    @DisplayName("전체 카드를 인기순으로 가져온다.")
    void getCardsAllWithHotSortType() {
        // given
        // data-test.sql
        final CardsAllRequest request = new CardsAllRequest(
            CardSortType.HOT,
            20L,
            15L
        );
        final Member member = memberService.findById(1L);

        // when

        final CardsAllResponse result = cardService.getCardAll(CacheMember.from(member), request);

        // then
        final List<CardsResponse> responses = result.result().cards();
        final CardsRequestResultType resultType = CardsRequestResultType.GET_CARDS_ALL_SUCCESS;
        assertSoftly(softly -> {
            softly.assertThat(result.code()).isEqualTo(resultType.getCode());
            softly.assertThat(result.message()).isEqualTo(resultType.getMessage());
            softly.assertThat(responses).hasSize(8);
            softly.assertThat(responses.get(0).cardId()).isEqualTo(19);
            softly.assertThat(responses.get(1).cardId()).isEqualTo(20);
            softly.assertThat(responses.get(2).cardId()).isEqualTo(21);
            softly.assertThat(responses.get(3).cardId()).isEqualTo(22);
            softly.assertThat(responses.get(4).cardId()).isEqualTo(23);
            softly.assertThat(responses.get(5).cardId()).isEqualTo(25);
            softly.assertThat(responses.get(6).cardId()).isEqualTo(27);
            softly.assertThat(responses.get(7).cardId()).isEqualTo(29);
        });
    }

    @Test
    @DisplayName("전체 카드를 최신순으로 가져온다.")
    void getCardsAllWithLatestSortType() {
        // given
        // data-test.sql
        final CardsAllRequest request = new CardsAllRequest(
            CardSortType.LATEST,
            5L,
            15L
        );
        final Member member = memberService.findById(2L);

        // when

        final CardsAllResponse result = cardService.getCardAll(CacheMember.from(member), request);

        // then
        final List<CardsResponse> responses = result.result().cards();
        final CardsRequestResultType resultType = CardsRequestResultType.GET_CARDS_ALL_SUCCESS;
        assertSoftly(softly -> {
            softly.assertThat(result.code()).isEqualTo(resultType.getCode());
            softly.assertThat(result.message()).isEqualTo(resultType.getMessage());
            softly.assertThat(responses).hasSize(3);
            softly.assertThat(responses.get(0).cardId()).isEqualTo(4);
            softly.assertThat(responses.get(1).cardId()).isEqualTo(3);
            softly.assertThat(responses.get(2).cardId()).isEqualTo(2);
        });
    }

    @Test
    @DisplayName("카드 작성에 대한 응답을 확인한다.")
    void writeResponseSuccess() {
        // given
        // data-test.sql
        final CardsWriteRequest request = new CardsWriteRequest(
                2L,
                mockingMultipartFile("test.jpg"),
                "hihi",
                "happy",
                "good"
        );

        final Member member = memberService.findById(1L);

        // when
        final CardsWriteResponse result = cardService.write(request, CacheMember.from(member));

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.code()).isEqualTo(1030);
            softly.assertThat(result.message()).isEqualTo("카드 작성에 성공하였습니다.");
        });
    }

    @Test
    @DisplayName("카드 작성 시 테마 ID가 존재하지 않을 경우를 테스트한다.")
    void writeResponseNotFoundErrorTheme() {
        // given
        // data-test.sql
        final CardsWriteRequest request = new CardsWriteRequest(
                10000L,
                mockingMultipartFile("test.jpg"),
                "hihi",
                "happy",
                "good"
        );

        final Member member = memberService.findById(1L);

        // when
        final NotFoundException exception
                = assertThrows(NotFoundException.class, () -> cardService.write(request, CacheMember.from(member)));

        // then
        assertEquals(ThemeExceptionType.NOT_FOUND_THEME.getCode(), exception.getExceptionType().getCode());
        assertEquals(ThemeExceptionType.NOT_FOUND_THEME.getMessage(), exception.getExceptionType().getMessage());
    }

    @Test
    @DisplayName("카드에 댓글을 작성한다.")
    void writeCommentOnCard() {
        // given
        // data-test.sql
        final long cardId = 5L;
        final Member member = memberService.findById(2L);

        // when
        final Long oldCommentCount = cardRepository.findById(cardId).get().getCommentCount();
        final WriteCommentOnCardResponse result = cardService.writeCommentOnCard(CacheMember.from(member), cardId);
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
        // data-test.sql
        final long cardId = cardRepository.count() + 1L;
        final Member member = memberService.findById(1L);
        final CacheMember cacheMember = CacheMember.from(member);

        // when, then
        assertThatThrownBy(() -> cardService.writeCommentOnCard(cacheMember, cardId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(CardExceptionType.NOT_FOUND_CARD.getMessage());
    }

    @Test
    @DisplayName("카드에 댓글 작성시, 이미 자신이 댓글 작성한 카드면 예외가 발생한다.")
    void writeCommentOnCardAlreadyExistException() {
        // given
        // data-test.sql
        final Member member = memberService.findById(1L);
        final long cardId = 2L;
        final CacheMember cacheMember = CacheMember.from(member);

        // when, then
        assertThatThrownBy(() -> cardService.writeCommentOnCard(cacheMember, cardId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(CardExceptionType.MY_COMMENT_ALREADY_EXIST.getMessage());
    }

    @Test
    @DisplayName("여러 사용자가 동시에 같은 카드의 댓글을 작성할 경우, 동기화 처리가 정상 동작된다.")
    void handleConcurrentCommentsOnCardSynchronously() {
        // given
        // data-test.sql
        final Member memberA = memberService.findById(1L);
        final Member memberB = memberService.findById(2L);
        long cardIdA = 1L;
        long cardIdB = 2L;

        CompletableFuture<Void> futureA =
                CompletableFuture.runAsync(() -> cardService.writeCommentOnCard(CacheMember.from(memberA), cardIdA));
        CompletableFuture<Void> futureB =
                CompletableFuture.runAsync(() -> cardService.writeCommentOnCard(CacheMember.from(memberB), cardIdB));

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(futureA, futureB);

        // when, then
        assertThatNoException().isThrownBy(combinedFuture::get);
    }

    @Test
    @DisplayName("카드를 삭제한다")
    void deleteCard() {
        // given
        // data-test.sql
        final Member member = memberService.findById(2L);

        // when
        final long oldCardCount = cardRepository.count();
        final DeleteCardResponse result = cardService.delete(CacheMember.from(member), 30L);

        // then
        final long newCardCount = cardRepository.count();
        assertSoftly(softly -> {
            final CardsRequestResultType resultType = CardsRequestResultType.DELETE_CARD_SUCCESS;
            softly.assertThat(result.code()).isEqualTo(resultType.getCode());
            softly.assertThat(result.message()).isEqualTo(resultType.getMessage());
            softly.assertThat(oldCardCount).isEqualTo(newCardCount + 1L);
        });
    }

    @Test
    @DisplayName("카드를 삭제할 시, 존재하지 않는 카드 ID이면 예외가 발생한다.")
    void deleteCardNotFoundException() {
        // given
        // data-test.sql
        final long cardId = cardRepository.count() + 1L;
        final Member member = memberService.findById(2L);

        // when, then
        assertThatThrownBy(() -> cardService.delete(CacheMember.from(member), cardId))
            .isInstanceOf(NotFoundException.class)
            .hasMessage(CardExceptionType.NOT_FOUND_CARD.getMessage());
    }

    @Test
    @DisplayName("카드를 삭제할 시, 자신이 쓴 카드가 아니면 예외가 발생한다.")
    void deleteNotCardWriterException() {
        // given
        // data-test.sql
        final long cardId = cardRepository.count();
        final Member member = memberService.findById(1L);

        // when, then
        assertThatThrownBy(() -> cardService.delete(CacheMember.from(member), cardId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(CardExceptionType.NOT_CARD_WRITER.getMessage());
    }

    @Test
    @DisplayName("카드 조회 시 정상 작동한다.")
    void getCommentOnCardWithCommentCountGreaterThanEqualThree() {
        // given
        // data-test.sql
        final Long cardId = 2L;
        final Member member = memberService.findById(1L);

        // when
        final GetCommentOnCardResponse result = cardService.getCommentOnCard(CacheMember.from(member), cardId);
        final CardsRequestResultType resultType = CardsRequestResultType.GET_COMMENT_ON_CARD_SUCCESS;

        // then
        final Long commentsCount = result.result().comments().stream().count();
        final Boolean hasComment = result.result().hasComment();
        assertSoftly(softly -> {
            softly.assertThat(commentsCount).isEqualTo(3);
            softly.assertThat(hasComment).isEqualTo(true);
            softly.assertThat(result.code()).isEqualTo(resultType.getCode());
            softly.assertThat(result.message()).isEqualTo(resultType.getMessage());
        });
    }

    @Test
    @DisplayName("카드에 댓글을 작성하지 않았을 경우 false를 반환한다.")
    void hasCommentOnCard() {
        // given
        // data-test.sql
        final Long cardId = 2L;
        final Member member = memberService.findById(2L);

        // when
        final GetCommentOnCardResponse result = cardService.getCommentOnCard(CacheMember.from(member), cardId);
        final CardsRequestResultType resultType = CardsRequestResultType.GET_COMMENT_ON_CARD_SUCCESS;

        // then
        final Long commentsCount = result.result().comments().stream().count();
        final Boolean hasComment = result.result().hasComment();
        assertSoftly(softly -> {
            softly.assertThat(commentsCount).isEqualTo(3L);
            softly.assertThat(hasComment).isEqualTo(false);
            softly.assertThat(result.code()).isEqualTo(resultType.getCode());
            softly.assertThat(result.message()).isEqualTo(resultType.getMessage());
        });
    }

    @Test
    @DisplayName("카드 조회 시 댓글이 2개 이하일 경우 정상 작동한다.")
    void getCommentOnCardWithCommentCountLessThanEqualTwo() {
        // given
        // data-test.sql
        final Long cardId = 1L;
        final Member member = memberService.findById(1L);

        // when
        final GetCommentOnCardResponse result = cardService.getCommentOnCard(CacheMember.from(member), cardId);
        final CardsRequestResultType resultType = CardsRequestResultType.GET_COMMENT_ON_CARD_SUCCESS;

        // then
        final Long commentsCount = result.result().comments().stream().count();
        assertSoftly(softly -> {
            softly.assertThat(commentsCount).isEqualTo(1);
            softly.assertThat(result.code()).isEqualTo(resultType.getCode());
            softly.assertThat(result.message()).isEqualTo(resultType.getMessage());
        });
    }

    @Test
    @DisplayName("댓글 조회 시 찾을 수 없는 카드인 경우 NotFound 예외가 발생한다.")
    void getCommentOnCardNotFoundException() {
        // given
        // data-test.sql
        final Long cardId = cardRepository.count() + 1L;
        final Member member = memberService.findById(1L);

        // when, then
        assertThatThrownBy(() -> cardService.getCommentOnCard(CacheMember.from(member), cardId))
            .isInstanceOf(NotFoundException.class)
            .hasMessage(CardExceptionType.NOT_FOUND_CARD.getMessage());
    }
    @Test
    @DisplayName("메인 조회 시 정상 작동한다.")
    void getMainHome() {
        // given
        // data-test.sql
        final Member member = memberService.findById(1L);

        // when
        final HomePageResponse result = cardService.home(CacheMember.from(member));
        final CardsRequestResultType resultType = CardsRequestResultType.GET_MAIN_HOME_SUCCESS;

        // then
        final List<TodayCardsOnHomeResponse> todayCards = result.result().todayCards();
        final int recentCardsCount = result.result().recentCards().size();
        final int recentThemeInfoCount = result.result().themes().size();

        assertSoftly(softly -> {
            softly.assertThat(result.code()).isEqualTo(resultType.getCode());
            softly.assertThat(result.message()).isEqualTo(resultType.getMessage());
            softly.assertThat(todayCards.size()).isEqualTo(3);
            softly.assertThat(recentCardsCount).isEqualTo(5);
            softly.assertThat(recentThemeInfoCount).isEqualTo(4);
            softly.assertThat(todayCards.get(0).cardId()).isEqualTo(3);
            softly.assertThat(todayCards.get(1).cardId()).isEqualTo(10);
            softly.assertThat(todayCards.get(2).cardId()).isEqualTo(1);
        });
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
