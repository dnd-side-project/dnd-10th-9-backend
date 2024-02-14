package com.dnd.dotchi.domain.card.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import com.dnd.dotchi.domain.card.dto.request.CardsByThemeRequest;
import com.dnd.dotchi.domain.card.dto.response.CardsByThemeResponse;
import com.dnd.dotchi.domain.card.dto.response.resultinfo.CardsByThemeRequestResultType;
import com.dnd.dotchi.domain.card.entity.vo.CardSortType;
import com.dnd.dotchi.domain.card.service.CardService;
import io.restassured.common.mapper.TypeRef;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CardController.class)
class CardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CardService cardService;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
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
                CardsByThemeRequestResultType.SUCCESS,
                List.of()
        );

        given(cardService.getCardsByTheme(request)).willReturn(response);

        // when
        final CardsByThemeResponse result = RestAssuredMockMvc.given().log().all()
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
                CardsByThemeRequestResultType.SUCCESS,
                List.of()
        );

        given(cardService.getCardsByTheme(request)).willReturn(response);

        // when, then
        RestAssuredMockMvc.given().log().all()
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

}
