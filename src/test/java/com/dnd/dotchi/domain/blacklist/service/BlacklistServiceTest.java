package com.dnd.dotchi.domain.blacklist.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.*;

import com.dnd.dotchi.domain.blacklist.dto.request.BlockRequest;
import com.dnd.dotchi.domain.blacklist.dto.response.BlockResponse;
import com.dnd.dotchi.domain.blacklist.dto.response.resulltinfo.BlockRequestResultType;
import com.dnd.dotchi.domain.blacklist.entity.BlackList;
import com.dnd.dotchi.domain.blacklist.repository.BlackListRepository;
import com.dnd.dotchi.domain.member.exception.MemberExceptionType;
import com.dnd.dotchi.global.exception.NotFoundException;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class BlacklistServiceTest {

    @Autowired
    BlacklistService blacklistService;

    @Autowired
    BlackListRepository blackListRepository;

    @Test
    @DisplayName("유저를 차단한다.")
    void report() {
        // given
        final long blacklisterId = 1L;
        final long blacklistedId = 2L;
        final BlockRequest request = new BlockRequest(blacklisterId);

        // when
        final BlockResponse response = blacklistService.block(blacklistedId, request);

        // then
        final List<BlackList> blackLists = blackListRepository.findAll();
        final BlackList report = blackLists.get(0);
        final BlockRequestResultType resultType = BlockRequestResultType.BLOCK_SUCCESS;
        assertSoftly(softly -> {
            softly.assertThat(blackLists).hasSize(1);
            softly.assertThat(report.getBlacklister().getId()).isEqualTo(blacklisterId);
            softly.assertThat(report.getBlacklisted().getId()).isEqualTo(blacklistedId);
            softly.assertThat(response.code()).isEqualTo(resultType.getCode());
            softly.assertThat(response.message()).isEqualTo(resultType.getMessage());
        });
    }

    @Test
    @DisplayName("차단할 시, blacklister ID가 존재하지 않을 때 예외가 발생한다.")
    void reportNotFoundReporterError() {
        // given
        final long blacklisterId = 0L;
        final long blacklistedId = 2L;
        final BlockRequest request = new BlockRequest(blacklisterId);

        // when, then
        assertThatThrownBy(() -> blacklistService.block(blacklistedId, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(MemberExceptionType.NOT_FOUND_MEMBER.getMessage());
    }

    @Test
    @DisplayName("차단할 시, blacklisted ID가 존재하지 않을 때 예외가 발생한다.")
    void reportNotFoundReportedError() {
        // given
        final long blacklisterId = 1L;
        final long blacklistedId = 0L;
        final BlockRequest request = new BlockRequest(blacklisterId);

        // when, then
        assertThatThrownBy(() -> blacklistService.block(blacklistedId, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(MemberExceptionType.NOT_FOUND_MEMBER.getMessage());
    }

    @AfterEach
    void tearDown() {
        blackListRepository.deleteAll();
    }

}