package com.dnd.dotchi.domain.blacklist.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.dnd.dotchi.domain.blacklist.dto.response.BlockResponse;
import com.dnd.dotchi.domain.blacklist.dto.response.resulltinfo.BlockRequestResultType;
import com.dnd.dotchi.domain.blacklist.entity.BlackList;
import com.dnd.dotchi.domain.blacklist.repository.BlackListRepository;
import com.dnd.dotchi.domain.member.entity.Member;
import com.dnd.dotchi.domain.member.exception.MemberExceptionType;
import com.dnd.dotchi.domain.member.repository.MemberRepository;
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
class BlacklistServiceTest {

    @Autowired
    BlacklistService blacklistService;

    @Autowired
    BlackListRepository blackListRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("유저를 차단한다.")
    void block() {
        // given
        // data-test.sql
        final long blacklisterId = 2L;
        final long blacklistedId = 1L;
        final Member member = memberRepository.findById(blacklisterId).get();

        // when
        final BlockResponse response = blacklistService.block(blacklistedId, CacheMember.from(member));

        // then
        final List<BlackList> blackLists = blackListRepository.findAll();
        final BlackList report = blackLists.get(1);
        final BlockRequestResultType resultType = BlockRequestResultType.BLOCK_SUCCESS;
        assertSoftly(softly -> {
            softly.assertThat(blackLists).hasSize(2);
            softly.assertThat(report.getBlacklister().getId()).isEqualTo(blacklisterId);
            softly.assertThat(report.getBlacklisted().getId()).isEqualTo(blacklistedId);
            softly.assertThat(response.code()).isEqualTo(resultType.getCode());
            softly.assertThat(response.message()).isEqualTo(resultType.getMessage());
        });
    }

    @Test
    @DisplayName("이미 차단한 유저이다.")
    void alreadyBlock() {
        // given
        final long blacklisterId = 1L;
        final long blacklistedId = 2L;
        final Member member = memberRepository.findById(blacklisterId).get();

        // when
        final BlockResponse response = blacklistService.block(blacklistedId, CacheMember.from(member));

        // then=
        final BlockRequestResultType resultType = BlockRequestResultType.ALREADY_BLOCK;
        assertSoftly(softly -> {
            softly.assertThat(response.code()).isEqualTo(resultType.getCode());
            softly.assertThat(response.message()).isEqualTo(resultType.getMessage());
        });
    }

    @Test
    @DisplayName("차단할 시, blacklisted ID가 존재하지 않을 때 예외가 발생한다.")
    void blockNotFoundBlacklistedError() {
        // given
        final long blacklisterId = 1L;
        final long blacklistedId = 0L;
        final Member member = memberRepository.findById(blacklisterId).get();

        // when, then
        assertThatThrownBy(() -> blacklistService.block(blacklistedId, CacheMember.from(member)))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(MemberExceptionType.NOT_FOUND_MEMBER.getMessage());
    }

    @AfterEach
    void tearDown() {
        blackListRepository.deleteAll();
    }

}
