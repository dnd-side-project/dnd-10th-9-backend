package com.dnd.dotchi.domain.report.service;

import com.dnd.dotchi.domain.member.entity.Member;
import com.dnd.dotchi.domain.member.exception.MemberExceptionType;
import com.dnd.dotchi.domain.member.repository.MemberRepository;
import com.dnd.dotchi.domain.report.dto.response.ReportResponse;
import com.dnd.dotchi.domain.report.dto.response.resultinfo.ReportRequestResultType;
import com.dnd.dotchi.domain.report.entity.Report;
import com.dnd.dotchi.domain.report.repository.ReportRepository;
import com.dnd.dotchi.domain.report.request.ReportRequest;
import com.dnd.dotchi.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;

    public ReportResponse report(
            final Long reportedId,
            final ReportRequest request,
            final Member member
    ) {
        final Member reported = memberRepository.findById(reportedId)
                .orElseThrow(() -> new NotFoundException(MemberExceptionType.NOT_FOUND_MEMBER));
        final Report report = Report.builder()
                .reporter(member)
                .reported(reported)
                .reason(request.reason())
                .build();

        reportRepository.save(report);
        return ReportResponse.from(ReportRequestResultType.REPORT_SUCCESS);
    }

}
