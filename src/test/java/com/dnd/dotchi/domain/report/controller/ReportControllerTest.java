package com.dnd.dotchi.domain.report.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.dnd.dotchi.domain.report.dto.response.ReportResponse;
import com.dnd.dotchi.domain.report.dto.response.resultinfo.ReportRequestResultType;
import com.dnd.dotchi.domain.report.request.ReportRequest;
import com.dnd.dotchi.domain.report.service.ReportService;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(ReportController.class)
class ReportControllerTest extends ControllerTest {

    @MockBean
    ReportService reportService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) throws Exception {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders
                        .standaloneSetup(new ReportController(reportService))
                        .setControllerAdvice(GlobalExceptionHandler.class)
        );
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
        mockingAuthArgumentResolver();
    }

    @Test
    @DisplayName("신고가 성공하면 200 응답을 반환한다.")
    void reportReturn200Success() {
        // given
        // data.sql
        final long reportedId = 2L;
        final String reason = "이상한 글을 씁니다.";
        final ReportRequest request = new ReportRequest(reason);
        final ReportResponse response = ReportResponse.from(ReportRequestResultType.REPORT_SUCCESS);

        given(reportService.report(anyLong(), any(), any())).willReturn(response);

        // when
        final ReportResponse result = RestAssuredMockMvc.given().log().all()
                .headers(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .pathParam("reportedId", reportedId)
                .body(request)
                .when().post("/reports/{reportedId}")
                .then().log().all()
                .status(HttpStatus.OK)
                .extract()
                .as(new TypeRef<>() {
                });

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    @DisplayName("신고가 실패하면 400 응답을 반환한다.")
    void reportReturn400BadRequest() {
        // given
        // data.sql
        final long reportedId = 2L;
        final String reason = "가".repeat(51);
        final ReportRequest request = new ReportRequest(reason);

        // when, then
        RestAssuredMockMvc.given().log().all()
                .headers(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .pathParam("reportedId", reportedId)
                .body(request)
                .when().post("/reports/{reportedId}")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body("code", equalTo(200))
                .body("message", containsString("신고 이유는 50자를 넘을 수 없습니다."));
    }

}
