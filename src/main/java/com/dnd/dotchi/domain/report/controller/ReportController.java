package com.dnd.dotchi.domain.report.controller;

import com.dnd.dotchi.domain.report.dto.response.ReportResponse;
import com.dnd.dotchi.domain.report.request.ReportRequest;
import com.dnd.dotchi.domain.report.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("{reportedId}")
    public ResponseEntity<ReportResponse> report(
            @PathVariable("reportedId") Long reportedId,
            @Valid @ModelAttribute ReportRequest request
    ) {
        final ReportResponse response = reportService.report(reportedId, request);
        return ResponseEntity.ok(response);
    }

}