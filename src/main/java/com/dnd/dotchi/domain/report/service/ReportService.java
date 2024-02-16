package com.dnd.dotchi.domain.report.service;

import com.dnd.dotchi.domain.report.dto.response.ReportResponse;
import com.dnd.dotchi.domain.report.dto.response.resultinfo.ReportRequestResultType;
import com.dnd.dotchi.domain.report.repository.ReportRepository;
import com.dnd.dotchi.domain.report.request.ReportRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ReportService {

    private final ReportRepository reportRepository;

    public ReportResponse report(final Long reportedId, final ReportRequest request) {
        return ReportResponse.from(ReportRequestResultType.REPORT_SUCCESS);
    }

}
