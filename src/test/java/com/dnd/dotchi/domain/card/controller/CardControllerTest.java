package com.dnd.dotchi.domain.card.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.dnd.dotchi.domain.card.dto.request.CardsAllRequest;
import com.dnd.dotchi.domain.card.dto.request.CardsByThemeRequest;
import com.dnd.dotchi.domain.card.dto.response.CardsAllResponse;
import com.dnd.dotchi.domain.card.dto.response.CardsByThemeResponse;
import com.dnd.dotchi.domain.card.dto.response.CardsWriteResponse;
import com.dnd.dotchi.domain.card.dto.response.DeleteCardResponse;
import com.dnd.dotchi.domain.card.dto.response.GetCommentOnCardResponse;
import com.dnd.dotchi.domain.card.dto.response.HomePageResponse;
import com.dnd.dotchi.domain.card.dto.response.WriteCommentOnCardResponse;
import com.dnd.dotchi.domain.card.dto.response.resultinfo.CardsRequestResultType;
import com.dnd.dotchi.domain.card.entity.Card;
import com.dnd.dotchi.domain.card.entity.Theme;
import com.dnd.dotchi.domain.card.entity.vo.CardSortType;
import com.dnd.dotchi.domain.card.service.CardService;
import com.dnd.dotchi.domain.member.entity.Member;
import com.dnd.dotchi.domain.member.service.MemberService;
import com.dnd.dotchi.global.exception.GlobalExceptionHandler;
import com.dnd.dotchi.infra.image.S3FileUploader;
import com.dnd.dotchi.test.ControllerTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(CardController.class)
class CardControllerTest extends ControllerTest {

    @MockBean
    CardService cardService;

    @MockBean
    S3FileUploader s3FileUploader;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) throws Exception {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders
                        .standaloneSetup(new CardController(cardService))
                        .setControllerAdvice(GlobalExceptionHandler.class)
        );
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
        mockingAuthArgumentResolver();
    }

    @Test
    @DisplayName("테마별 카드 조회에 성공하면 200 응답을 반환한다.")
    void getCardsByThemeReturn200Success() {
        // given
        // data-test.sql
        final CardsByThemeRequest request = new CardsByThemeRequest(
                1L,
                CardSortType.HOT,
                1L,
                1L
        );

        final CardsByThemeResponse response = CardsByThemeResponse.of(
                CardsRequestResultType.GET_CARDS_BY_THEME_SUCCESS,
                List.of()
        );

        final Member member = memberService.findById(1L);

        given(cardService.getCardsByTheme(member, request)).willReturn(response);

        // when
        final CardsByThemeResponse result = RestAssuredMockMvc.given().log().all()
                .headers(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                .queryParam("themeId", 1L)
                .queryParam("cardSortType", CardSortType.HOT)
                .queryParam("lastCardId", 1L)
                .queryParam("lastCardCommentCount", 1L)
                .when().get("/cards/theme")
                .then().log().all()
                .status(HttpStatus.OK)
                .extract()
                .as(new TypeRef<>() {
                });

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    @DisplayName("테마별 카드 조회에 잘못된 값으로 요청하면 400 응답을 반환한다.")
    void getCardsByThemeReturn400BadRequest() {
        // given

        // when, then
        RestAssuredMockMvc.given().log().all()
                .headers(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                .queryParam("themeId", 0L)
                .queryParam("cardSortType", CardSortType.HOT)
                .queryParam("lastCardId", 0L)
                .queryParam("lastCardCommentCount", -1L)
                .when().get("/cards/theme")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body("code", equalTo(200))
                .body("message", containsString("테마 ID는 양수만 가능합니다."))
                .body("message", containsString("마지막 조회 가드 ID는 양수만 가능합니다."))
                .body("message", containsString("마지막으로 조회한 카드의 댓글 수는 0 이상만 가능합니다."));
    }

    @Test
    @DisplayName("댓글 작성에 성공하면 200 응답을 반환한다.")
    void writeCommentOnCardReturn200Success() {
        // given
        // data-test.sql
        final WriteCommentOnCardResponse response =
                WriteCommentOnCardResponse.from(CardsRequestResultType.WRITE_COMMENT_ON_CARD_SUCCESS);
        given(cardService.writeCommentOnCard(any(), anyLong())).willReturn(response);

        // when
        final WriteCommentOnCardResponse result = RestAssuredMockMvc.given().log().all()
                .headers(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                .pathParam("cardId", 1L)
                .when().post("/cards/{cardId}/comments")
                .then().log().all()
                .status(HttpStatus.OK)
                .extract()
                .as(new TypeRef<>() {
                });

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    @DisplayName("카드 작성을 성공하면 200 응답을 반환한다.")
    void cardsWriteReturn200Success() {
        // given
        final String contentBody = "image";

        final CardsWriteResponse response = CardsWriteResponse.from(CardsRequestResultType.WRITE_CARDS_SUCCESS);
        given(cardService.write(any(), any())).willReturn(response);

        // when

        final CardsWriteResponse result = RestAssuredMockMvc.given().log().all()
                .headers(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .formParam("themeId", 1L)
                .multiPart("image", contentBody, MediaType.IMAGE_PNG_VALUE)
                .formParam("backName", "따봉도치")
                .formParam("backMood", "행복해")
                .formParam("backContent", "아무거나")
                .when().post("/cards")
                .then().log().all()
                .status(HttpStatus.OK)
                .extract()
                .as(new TypeRef<>() {
                });

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    @DisplayName("잘못된 값으로 카드를 작성 하면 400 응답을 반환한다.")
    void cardsWriteReturn400BadRequestFromArgument() {
        // given
        final String contentBody = "image";

        // when, then
        RestAssuredMockMvc.given().log().all()
                .headers(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .formParam("themeId", "")
                .multiPart("image", contentBody, MediaType.IMAGE_JPEG_VALUE)
                .formParam("backName", "제목 넘지마 따봉도치")
                .formParam("backMood", "엄지가 절로 올라가지마 따봉도치")
                .formParam("backContent", "따봉도치 20글자 절대로 넘지마 확인할거니까")
                .when().post("/cards")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body("code", equalTo(200))
                .body("message", containsString("테마 ID는 빈 값일 수 없습니다."))
                .body("message", containsString("따봉네임은 7자를 넘을 수 없습니다."))
                .body("message", containsString("오늘의 기분은 15자를 넘을 수 없습니다."))
                .body("message", containsString("따봉 디테일은 20자를 넘을 수 없습니다."));

    }

    @Test
    @DisplayName("카드 작성시 이미지 파일이 아닐 경우 400 응답을 반환한다.")
    void cardsWriteReturn400BadRequestFromImage() throws IOException {
        // given
        final String contentBody = "noImage";

        // when, then
        RestAssuredMockMvc.given().log().all()
                .headers(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .formParam("themeId", 1L)
                .multiPart("image", contentBody, MediaType.TEXT_PLAIN_VALUE)
                .formParam("backName", "따봉도치")
                .formParam("backMood", "엄지가 절로 올라가")
                .formParam("backContent", "따봉도치 20글자")
                .when().post("/cards")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body("code", equalTo(800))
                .body("message", equalTo("유효하지 않은 이미지 정보입니다."));

    }

    @Test
    @DisplayName("전체 카드 조회에 성공하면 200 응답을 반환한다.")
    void getCardsAllReturn200Success() {
        // given
        // data.sql
        final CardsAllRequest request = new CardsAllRequest(
                CardSortType.HOT,
                1L,
                1L
        );

        final CardsAllResponse response = CardsAllResponse.of(
                CardsRequestResultType.GET_CARDS_ALL_SUCCESS,
                List.of()
        );

        final Member member = memberService.findById(1L);
        given(cardService.getCardAll(member, request)).willReturn(response);

        // when
        final CardsAllResponse result = RestAssuredMockMvc.given().log().all()
                .headers(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                .queryParam("cardSortType", CardSortType.HOT)
                .queryParam("lastCardId", 1L)
                .queryParam("lastCardCommentCount", 1L)
                .when().get("/cards/all")
                .then().log().all()
                .status(HttpStatus.OK)
                .extract()
                .as(new TypeRef<>() {
                });

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    @DisplayName("전체 카드 조회에 잘못된 값으로 요청하면 400 응답을 반환한다.")
    void getCardsAllReturn400BadRequest() {
        // given

        // when, then
        RestAssuredMockMvc.given().log().all()
                .headers(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                .queryParam("cardSortType", CardSortType.HOT)
                .queryParam("lastCardId", 0L)
                .queryParam("lastCardCommentCount", -1L)
                .when().get("/cards/all")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body("code", equalTo(200))
                .body("message", containsString("마지막으로 조회한 카드 ID는 양수만 가능합니다."))
                .body("message", containsString("마지막으로 조회한 카드의 댓글 수는 0 이상만 가능합니다."));
    }

    @Test
    @DisplayName("카드 삭제에 성공하면 200 응답을 반환한다.")
    void deleteCardReturn200Success() {
        // given
        final long cardId = 1L;
        final DeleteCardResponse response = DeleteCardResponse.from(CardsRequestResultType.DELETE_CARD_SUCCESS);

        given(cardService.delete(any(), anyLong())).willReturn(response);

        // when
        final DeleteCardResponse result = RestAssuredMockMvc.given().log().all()
                .headers(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                .pathParam("cardId", cardId)
                .when().delete("/cards/{cardId}")
                .then().log().all()
                .status(HttpStatus.OK)
                .extract()
                .as(new TypeRef<>() {
                });

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    @DisplayName("댓글 조회에 대해 성공하면 200을 반환한다.")
    void getCommentsReturn200SuccessRequest() {
        // given
        final Member member = memberService.findById(1L);
        final Theme theme = new Theme("test");
        final Card card = new Card(
                member, theme, "test", "test", "test", "test"
        );

        final GetCommentOnCardResponse response =
                GetCommentOnCardResponse.of(
                        card,
                        List.of(),
                        null,
                        CardsRequestResultType.GET_COMMENT_ON_CARD_SUCCESS
                );

        given(cardService.getCommentOnCard(any(), anyLong())).willReturn(response);

        // when
        final GetCommentOnCardResponse result = RestAssuredMockMvc.given().log().all()
                .headers(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                .pathParam("cardId", 1L)
                .when().get("/cards/{cardId}/comments")
                .then().log().all()
                .status(HttpStatus.OK)
                .extract()
                .as(new TypeRef<>() {
                });

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    @DisplayName("메인 홈 조회를 성공하면 200을 반환한다.")
    void getHomeReturn400BadRequest() {
        // given
        final HomePageResponse response =
            HomePageResponse.of(
                CardsRequestResultType.GET_COMMENT_ON_CARD_SUCCESS,
                List.of(),
                List.of(),
                List.of()
            );

        given(cardService.home(any())).willReturn(response);

        // when
        final HomePageResponse result = RestAssuredMockMvc.given().log().all()
            .headers(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
            .when().get("/cards/main")
            .then().log().all()
            .status(HttpStatus.OK)
            .extract()
            .as(new TypeRef<>() {
            });

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(response);
    }

}
