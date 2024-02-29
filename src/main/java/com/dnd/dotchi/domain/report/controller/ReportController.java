package com.dnd.dotchi.domain.report.controller;

import com.dnd.dotchi.domain.report.dto.response.ReportResponse;
import com.dnd.dotchi.domain.report.request.ReportRequest;
import com.dnd.dotchi.domain.report.service.ReportService;
import com.dnd.dotchi.global.jwt.Auth;
import com.dnd.dotchi.global.redis.CacheMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController implements ReportControllerDocs {

    private final ReportService reportService;

    @PostMapping("{reportedId}")
    public ResponseEntity<ReportResponse> report(
            @Auth final CacheMember member,
            @PathVariable("reportedId") final Long reportedId,
            @Valid @RequestBody final ReportRequest request
    ) {
        final ReportResponse response = reportService.report(reportedId, request, member);
        return ResponseEntity.ok(response);
    }

}
