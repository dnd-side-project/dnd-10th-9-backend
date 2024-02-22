package com.dnd.dotchi.domain.blacklist.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.dnd.dotchi.domain.blacklist.dto.response.BlockResponse;
import com.dnd.dotchi.domain.blacklist.dto.response.resulltinfo.BlockRequestResultType;
import com.dnd.dotchi.domain.blacklist.service.BlacklistService;
import com.dnd.dotchi.domain.member.entity.Member;
import com.dnd.dotchi.global.exception.GlobalExceptionHandler;
import com.dnd.dotchi.test.ControllerTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(BlacklistController.class)
class BlacklistControllerTest extends ControllerTest {

    @MockBean
    BlacklistService blacklistService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) throws Exception {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders
                        .standaloneSetup(new BlacklistController(blacklistService))
                        .setControllerAdvice(GlobalExceptionHandler.class)
        );
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
        mockingAuthArgumentResolver();
    }

    @Test
    @DisplayName("차단에 성공하면 200 응답을 반환한다.")
    void blockReturn200Success() {
        // given
        final long blacklistedId = 2L;
        final BlockResponse response = BlockResponse.from(BlockRequestResultType.BLOCK_SUCCESS);

        given(blacklistService.block(anyLong(), any())).willReturn(response);

        // when
        final BlockResponse result = RestAssuredMockMvc.given().log().all()
            .headers(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
            .pathParam("blacklistedId", blacklistedId)
            .when().post("/blacklists/{blacklistedId}")
            .then().log().all()
            .status(HttpStatus.OK)
            .extract()
            .as(new TypeRef<>() {
            });

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    @DisplayName("이미 차단한 유저이면 202 응답을 반환한다.")
    void blockReturn202Success() {
        // given
        final long blacklistedId = 2L;
        final BlockResponse response = BlockResponse.from(BlockRequestResultType.ALREADY_BLOCK);

        given(blacklistService.block(anyLong(), any())).willReturn(response);

        // when
        final BlockResponse result = RestAssuredMockMvc.given().log().all()
            .headers(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
            .pathParam("blacklistedId", blacklistedId)
            .when().post("/blacklists/{blacklistedId}")
            .then().log().all()
            .status(HttpStatus.ACCEPTED)
            .extract()
            .as(new TypeRef<>() {
            });

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(response);
    }

}
