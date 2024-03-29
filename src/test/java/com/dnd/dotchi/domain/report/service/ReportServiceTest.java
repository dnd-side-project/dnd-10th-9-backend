package com.dnd.dotchi.domain.report.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.dnd.dotchi.domain.member.entity.Member;
import com.dnd.dotchi.domain.member.exception.MemberExceptionType;
import com.dnd.dotchi.domain.member.repository.MemberRepository;
import com.dnd.dotchi.domain.report.dto.response.ReportResponse;
import com.dnd.dotchi.domain.report.dto.response.resultinfo.ReportRequestResultType;
import com.dnd.dotchi.domain.report.entity.Report;
import com.dnd.dotchi.domain.report.repository.ReportRepository;
import com.dnd.dotchi.domain.report.request.ReportRequest;
import com.dnd.dotchi.global.exception.NotFoundException;
import com.dnd.dotchi.global.redis.CacheMember;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class ReportServiceTest {

    @Autowired
    ReportService reportService;

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("유저를 신고한다.")
    void report() {
        // given
        final long reporterId = 1L;
        final long reportedId = 2L;
        final String reason = "이상한 글을 씁니다.";
        final ReportRequest request = new ReportRequest(reason);
        final Member member = memberRepository.findById(reporterId).get();

        // when
        final ReportResponse response = reportService.report(reportedId, request, CacheMember.from(member));

        // then
        final List<Report> reports = reportRepository.findAll();
        final Report report = reports.get(0);
        final ReportRequestResultType resultType = ReportRequestResultType.REPORT_SUCCESS;
        assertSoftly(softly -> {
            softly.assertThat(reports).hasSize(1);
            softly.assertThat(report.getReporter().getId()).isEqualTo(reporterId);
            softly.assertThat(report.getReported().getId()).isEqualTo(reportedId);
            softly.assertThat(response.code()).isEqualTo(resultType.getCode());
            softly.assertThat(response.message()).isEqualTo(resultType.getMessage());
        });
    }

    @Test
    @DisplayName("신고할 시, 신고 당한 유저 ID가 존재하지 않을 때 예외가 발생한다.")
    void reportNotFoundReportedError() {
        // given
        final long reporterId = 1L;
        final long reportedId = 0L;
        final String reason = "이상한 글을 씁니다.";
        final ReportRequest request = new ReportRequest(reason);
        final Member member = memberRepository.findById(reporterId).get();

        // when, then
        assertThatThrownBy(() -> reportService.report(reportedId, request, CacheMember.from(member)))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(MemberExceptionType.NOT_FOUND_MEMBER.getMessage());
    }

    @AfterEach
    void tearDown() {
        reportRepository.deleteAll();
    }
}
