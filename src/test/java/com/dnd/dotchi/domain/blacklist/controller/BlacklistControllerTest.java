package com.dnd.dotchi.domain.blacklist.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import com.dnd.dotchi.domain.blacklist.dto.request.BlockRequest;
import com.dnd.dotchi.domain.blacklist.dto.response.BlockResponse;
import com.dnd.dotchi.domain.blacklist.dto.response.resulltinfo.BlockRequestResultType;
import com.dnd.dotchi.domain.blacklist.service.BlacklistService;
import com.dnd.dotchi.domain.card.controller.CardController;
import com.dnd.dotchi.domain.card.dto.request.CardsByThemeRequest;
import com.dnd.dotchi.domain.card.dto.response.CardsByThemeResponse;
import com.dnd.dotchi.domain.card.dto.response.resultinfo.CardsRequestResultType;
import com.dnd.dotchi.domain.card.entity.vo.CardSortType;
import com.dnd.dotchi.global.exception.GlobalExceptionHandler;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(BlacklistController.class)
class BlacklistControllerTest {

    @MockBean
    BlacklistService blacklistService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders
                        .standaloneSetup(new BlacklistController(blacklistService))
                        .setControllerAdvice(GlobalExceptionHandler.class)
        );
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    @DisplayName("차단에 성공하면 200 응답을 반환한다.")
    void blockReturn200Success() {
        // given
        final long blacklisterId = 1L;
        final long blacklistedId = 2L;
        final BlockRequest request = new BlockRequest(blacklisterId);
        final BlockResponse response = BlockResponse.from(BlockRequestResultType.BLOCK_SUCCESS);

        given(blacklistService.block(blacklistedId, request)).willReturn(response);

        // when
        final BlockResponse result = RestAssuredMockMvc.given().log().all()
                .contentType(MediaType.APPLICATION_JSON)
                .pathParam("blacklistedId", blacklistedId)
                .body(request)
                .when().post("/blacklists/{blacklistedId}")
                .then().log().all()
                .status(HttpStatus.OK)
                .extract()
                .as(new TypeRef<>() {
                });

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(response);
    }

}
