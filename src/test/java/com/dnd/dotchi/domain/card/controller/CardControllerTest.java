package com.dnd.dotchi.domain.card.controller;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.hibernate.validator.internal.util.Contracts.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.dnd.dotchi.domain.card.dto.request.CardsByThemeRequest;
import com.dnd.dotchi.domain.card.dto.request.CardsWriteRequest;
import com.dnd.dotchi.domain.card.dto.response.CardsByThemeResponse;
import com.dnd.dotchi.domain.card.dto.response.CardsWriteResponse;
import com.dnd.dotchi.domain.card.dto.response.resultinfo.CardsRequestResultType;
import com.dnd.dotchi.domain.card.entity.vo.CardSortType;
import com.dnd.dotchi.domain.card.exception.CardExceptionType;
import com.dnd.dotchi.domain.card.service.CardService;
import com.dnd.dotchi.global.exception.BadRequestException;
import com.dnd.dotchi.global.exception.GlobalExceptionHandler;
import com.dnd.dotchi.infra.image.ImageUploader;

import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.specification.MultiPartSpecification;

@WebMvcTest(CardController.class)
class CardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CardService cardService;

    @MockBean
    ImageUploader imageUploader;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        RestAssuredMockMvc.standaloneSetup(
            MockMvcBuilders
                .standaloneSetup(new CardController(cardService))
                .setControllerAdvice(GlobalExceptionHandler.class)
        );
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    @DisplayName("테마별 카드 조회에 성공하면 200 응답을 반환한다.")
    void getCardsByThemeReturn200Success() {
        // given
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

        given(cardService.getCardsByTheme(request)).willReturn(response);

        // when
        final CardsByThemeResponse result = RestAssuredMockMvc.given().log().all()
                .contentType(MediaType.APPLICATION_JSON)
                .param("themeId", 1L)
                .param("cardSortType", CardSortType.HOT)
                .param("lastCardId", 1L)
                .param("lastCardCommentCount", 1L)
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

        given(cardService.getCardsByTheme(request)).willReturn(response);

        // when, then
        RestAssuredMockMvc.given().log().all()
                .contentType(MediaType.APPLICATION_JSON)
                .param("themeId", 0L)
                .param("cardSortType", CardSortType.HOT)
                .param("lastCardId", 0L)
                .param("lastCardCommentCount", 0L)
                .when().get("/cards/theme")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body("code", equalTo(200))
                .body("message", containsString("테마 ID는 양수만 가능합니다."))
                .body("message", containsString("마지막 조회 가드 ID는 양수만 가능합니다."))
                .body("message", containsString("마지막 조회 카드의 댓글 개수는 양수만 가능합니다."));
    }

    @Test
    @DisplayName("카드 작성을 성공하면 200 응답을 반환한다.")
    void cardsWriteReturn200Success() throws IOException {
        // given
        final String contenBody = "image";

        final CardsWriteResponse response = CardsWriteResponse.of(CardsRequestResultType.WRITE_CARDS_SUCCESS);
        given(cardService.write(any())).willReturn(response);

        // when

        final CardsWriteResponse result = RestAssuredMockMvc.given().log().all()
            .contentType(ContentType.MULTIPART)
            .param("memberId", 1L)
            .param("themeId", 1L)
            .multiPart("image", contenBody, MediaType.IMAGE_PNG_VALUE)
            .param("backName", "따봉도치")
            .param("backMood", "행복해")
            .param("backContent", "아무거나")
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
        final String contenBody = "image";

        // when, then
        RestAssuredMockMvc.given().log().all()
            .contentType(ContentType.MULTIPART)
            .param("memberId", 0L)
            .param("themeId", "")
            .multiPart("image", contenBody, MediaType.IMAGE_JPEG_VALUE)
            .param("backName", "제목 넘지마 따봉도치")
            .param("backMood", "엄지가 절로 올라가지마 따봉도치")
            .param("backContent", "따봉도치 20글자 절대로 넘지마 확인할거니까")
            .when().post("/cards")
            .then().log().all()
            .status(HttpStatus.BAD_REQUEST)
            .body("code", equalTo(200))
            .body("message", containsString("멤버 ID는 양수만 가능합니다."))
            .body("message", containsString("테마 ID는 빈 값일 수 없습니다."))
            .body("message", containsString("따봉네임은 7자를 넘을 수 없습니다."))
            .body("message", containsString("오늘의 기분은 15자를 넘을 수 없습니다."))
            .body("message", containsString("따봉 디테일은 20자를 넘을 수 없습니다."));

    }

    @Test
    @DisplayName("카드 작성시 이미지 파일이 아닐 경우 400 응답을 반환한다.")
    void cardsWriteReturn400BadRequestFromImage() throws IOException {
        // given
        final String contenBody = "noImage";

        // when, then
        RestAssuredMockMvc.given().log().all()
            .contentType(ContentType.MULTIPART)
            .param("memberId", 1L)
            .param("themeId", 1L)
            .multiPart("image", contenBody, MediaType.TEXT_PLAIN_VALUE)
            .param("backName", "따봉도치")
            .param("backMood", "엄지가 절로 올라가")
            .param("backContent", "따봉도치 20글자")
            .when().post("/cards")
            .then().log().all()
            .status(HttpStatus.BAD_REQUEST)
            .body("code", equalTo(800))
            .body("message", equalTo("유효하지 않은 이미지 정보입니다."));

    }

}
