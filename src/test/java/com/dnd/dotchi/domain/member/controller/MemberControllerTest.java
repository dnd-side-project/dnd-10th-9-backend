package com.dnd.dotchi.domain.member.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;

import com.dnd.dotchi.domain.member.dto.response.MemberInfoResponse;
import com.dnd.dotchi.domain.member.dto.response.resultinfo.MemberRequestResultType;
import com.dnd.dotchi.domain.member.entity.Member;
import com.dnd.dotchi.domain.member.service.MemberService;
import com.dnd.dotchi.global.exception.GlobalExceptionHandler;
import io.restassured.common.mapper.TypeRef;
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

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @MockBean
    MemberService memberService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders
                        .standaloneSetup(new MemberController(memberService))
                        .setControllerAdvice(GlobalExceptionHandler.class)
        );
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    @DisplayName("회원 정보 조회에 성공하면 200 응답을 반환한다.")
    void getMemberInfoReturn200Success() {
        // given
        final Long memberId = 1L;
        final Long lastCardId = 25L;

        final MemberInfoResponse response = MemberInfoResponse.of(
                MemberRequestResultType.GET_MEMBER_INFO_SUCCESS,
                Member.builder().build(),
                List.of()
        );

        given(memberService.getMemberInfo(memberId, lastCardId)).willReturn(response);

        // when
        final MemberInfoResponse result = RestAssuredMockMvc.given().log().all()
                .contentType(MediaType.APPLICATION_JSON)
                .pathParam("memberId", memberId)
                .param("lastCardId", lastCardId)
                .when().get("/members/{memberId}")
                .then().log().all()
                .status(HttpStatus.OK)
                .extract()
                .as(new TypeRef<>() {
                });

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    @DisplayName("회원 정보 조회에 실패하면 400 응답을 반환한다.")
    void getMemberInfoReturn400BadRequest() {
        // given
        final Long memberId = 1L;
        final Long lastCardId = 0L;

        // when, then
        RestAssuredMockMvc.given().log().all()
                .contentType(MediaType.APPLICATION_JSON)
                .pathParam("memberId", memberId)
                .param("lastCardId", lastCardId)
                .when().get("/members/{memberId}")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body("code", equalTo(200))
                .body("message", containsString("마지막 조회 가드 ID는 양수만 가능합니다."));
    }

}
