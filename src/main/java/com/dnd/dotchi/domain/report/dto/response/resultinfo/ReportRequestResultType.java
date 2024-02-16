package com.dnd.dotchi.domain.report.dto.response.resultinfo;

import lombok.Getter;

@Getter
public enum ReportRequestResultType {
    REPORT_SUCCESS(1300, "신고가 성공하였습니다.");

    private final int code;
    private final String message;

    ReportRequestResultType(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

}
