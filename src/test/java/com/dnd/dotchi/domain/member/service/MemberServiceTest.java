package com.dnd.dotchi.domain.member.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.dnd.dotchi.domain.member.dto.response.MemberInfoResponse;
import com.dnd.dotchi.domain.member.dto.response.MemberInfoResultResponse;
import com.dnd.dotchi.domain.member.dto.response.RecentCardsByMemberResponse;
import com.dnd.dotchi.domain.member.dto.response.resultinfo.MemberRequestResultType;
import com.dnd.dotchi.domain.member.exception.MemberExceptionType;
import com.dnd.dotchi.domain.member.repository.MemberRepository;
import com.dnd.dotchi.global.exception.NotFoundException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원 정보 조회")
    void getMemberInfo() {
        // given
        // data.sql
        final long memberId = 1L;
        final long lastCardId = 25L;

        // when
        final MemberInfoResponse response = memberService.getMemberInfo(memberId, lastCardId);

        // then
        final MemberRequestResultType resultType = MemberRequestResultType.GET_MEMBER_INFO_SUCCESS;
        final MemberInfoResultResponse resultResponse = response.result();
        final List<RecentCardsByMemberResponse> recentCards = resultResponse.recentCards();
        assertSoftly(softly -> {
            softly.assertThat(response.code()).isEqualTo(resultType.getCode());
            softly.assertThat(response.message()).isEqualTo(resultType.getMessage());
            softly.assertThat(resultResponse.member().id()).isEqualTo(memberId);
            softly.assertThat(recentCards).hasSize(10);
            softly.assertThat(recentCards.get(0).cardId()).isEqualTo(23);
            softly.assertThat(recentCards.get(1).cardId()).isEqualTo(21);
            softly.assertThat(recentCards.get(2).cardId()).isEqualTo(19);
            softly.assertThat(recentCards.get(3).cardId()).isEqualTo(17);
            softly.assertThat(recentCards.get(4).cardId()).isEqualTo(15);
            softly.assertThat(recentCards.get(5).cardId()).isEqualTo(13);
            softly.assertThat(recentCards.get(6).cardId()).isEqualTo(11);
            softly.assertThat(recentCards.get(7).cardId()).isEqualTo(9);
            softly.assertThat(recentCards.get(8).cardId()).isEqualTo(7);
            softly.assertThat(recentCards.get(9).cardId()).isEqualTo(5);
        });
    }

    @Test
    @DisplayName("회원 정보 조회 시, 없는 회원 ID이면 예외가 발생한다.")
    void getMemberInfoNotFoundException() {
        // given
        // data.sql
        final long memberId = memberRepository.count() + 1L;
        final long lastCardId = 25L;

        // when, then
        assertThatThrownBy(() -> memberService.getMemberInfo(memberId, lastCardId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(MemberExceptionType.NOT_FOUND_MEMBER.getMessage());
    }

}
