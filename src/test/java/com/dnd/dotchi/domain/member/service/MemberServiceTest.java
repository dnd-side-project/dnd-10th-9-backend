package com.dnd.dotchi.domain.member.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.amazonaws.services.s3.AmazonS3;
import com.dnd.dotchi.domain.member.dto.request.MemberAuthorizationRequest;
import com.dnd.dotchi.domain.member.dto.request.MemberInfoRequest;
import com.dnd.dotchi.domain.member.dto.request.MemberModifyRequest;
import com.dnd.dotchi.domain.member.dto.response.MemberAuthorizationResponse;
import com.dnd.dotchi.domain.member.dto.response.MemberAuthorizationResultResponse;
import com.dnd.dotchi.domain.member.dto.response.MemberInfoResponse;
import com.dnd.dotchi.domain.member.dto.response.MemberInfoResultResponse;
import com.dnd.dotchi.domain.member.dto.response.MemberModifyResponse;
import com.dnd.dotchi.domain.member.dto.response.RecentCardsByMemberResponse;
import com.dnd.dotchi.domain.member.dto.response.resultinfo.MemberRequestResultType;
import com.dnd.dotchi.domain.member.entity.Member;
import com.dnd.dotchi.domain.member.exception.MemberExceptionType;
import com.dnd.dotchi.domain.member.repository.MemberRepository;
import com.dnd.dotchi.global.exception.NotFoundException;
import com.dnd.dotchi.global.jwt.TokenPayload;
import com.dnd.dotchi.global.jwt.TokenProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TokenProcessor tokenProcessor;

    @MockBean
    private AmazonS3 amazonS3Mock;

    @BeforeEach
    void setUp() throws MalformedURLException, URISyntaxException {
        URI uri = new URI("http://example.com/uploaded/image.jpg");
        given(amazonS3Mock.getUrl(anyString(), anyString())).willReturn(uri.toURL());
    }

    @Test
    @DisplayName("회원 정보 조회")
    void getMemberInfo() {
        // given
        // data-test.sql
        final Member member = memberService.findById(1L);
        final long lastCardId = 25L;

        // when
        final MemberInfoResponse response = memberService.getMemberInfo(member, lastCardId);

        // then
        final MemberRequestResultType resultType = MemberRequestResultType.GET_MEMBER_INFO_SUCCESS;
        final MemberInfoResultResponse resultResponse = response.result();
        final List<RecentCardsByMemberResponse> recentCards = resultResponse.recentCards();
        assertSoftly(softly -> {
            softly.assertThat(response.code()).isEqualTo(resultType.getCode());
            softly.assertThat(response.message()).isEqualTo(resultType.getMessage());
            softly.assertThat(resultResponse.member().id()).isEqualTo(member.getId());
            softly.assertThat(recentCards).hasSize(7);
            softly.assertThat(recentCards.get(0).cardId()).isEqualTo(23);
            softly.assertThat(recentCards.get(1).cardId()).isEqualTo(21);
            softly.assertThat(recentCards.get(2).cardId()).isEqualTo(17);
            softly.assertThat(recentCards.get(3).cardId()).isEqualTo(15);
            softly.assertThat(recentCards.get(4).cardId()).isEqualTo(7);
            softly.assertThat(recentCards.get(5).cardId()).isEqualTo(5);
            softly.assertThat(recentCards.get(6).cardId()).isEqualTo(1);
        });
    }

    @Test
    @DisplayName("로그인 한다.")
    void login() throws JsonProcessingException {
        // given
        // data-test.sql
        final long memberId = 1L;
        final MemberAuthorizationRequest request = new MemberAuthorizationRequest(memberId);

        // when
        final MemberAuthorizationResponse response = memberService.login(request);

        // then
        final MemberRequestResultType resultType = MemberRequestResultType.LOGIN_SUCCESS;
        final MemberAuthorizationResultResponse result = response.result();
        final String resolveToken = tokenProcessor.resolveToken("Bearer " + result.accessToken());
        final TokenPayload tokenPayload = tokenProcessor.parseToken(resolveToken);

        assertSoftly(softly -> {
            softly.assertThat(response.code()).isEqualTo(resultType.getCode());
            softly.assertThat(response.message()).isEqualTo(resultType.getMessage());
            softly.assertThat(result.memberId()).isEqualTo(memberId);
            softly.assertThat(tokenPayload.memberId()).isEqualTo(memberId);
            softly.assertThat(result.memberName()).isEqualTo("회원1");
            softly.assertThat(result.memberImageUrl()).isEqualTo("http://example.com/image1.jpg");
            softly.assertThat(result.accessToken()).isEqualTo(resolveToken);
        });
    }

    @Test
    @DisplayName("로그인 시, 존재하지 않는 회원 ID이면 예외가 발생한다.")
    void loginNotFoundException() {
        // given
        // data-test.sql
        final long memberId = 0L;
        final MemberAuthorizationRequest request = new MemberAuthorizationRequest(memberId);

        // when, then
        assertThatThrownBy(() -> memberService.login(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(MemberExceptionType.NOT_FOUND_MEMBER.getMessage());
    }

    @Test
    @DisplayName("회원 정보 수정 성공, 이미지 포함")
    void patchMemberInfoHasImage() {
        // given
        // data.sql
        final Member member = memberService.findById(1L);
        final MockMultipartFile image
            = new MockMultipartFile("img", "img", "image/png", "img".getBytes());
        final MemberModifyRequest request
            = new MemberModifyRequest(Optional.of(image),"오뜨","멍청이");

        // when
        final MemberModifyResponse response = memberService.patchMemberInfo(member, request);

        // then
        final MemberRequestResultType resultType = MemberRequestResultType.PATCH_MEMBER_INFO_SUCCESS;
        assertSoftly(softly -> {
            softly.assertThat(response.code()).isEqualTo(resultType.getCode());
            softly.assertThat(response.message()).isEqualTo(resultType.getMessage());
        });
    }

    @Test
    @DisplayName("회원 정보 수정 성공, 이미지 미포함")
    void patchMemberInfoNoImage() {
        // given
        // data.sql
        final Member member = memberService.findById(1L);
        final MemberModifyRequest request
            = new MemberModifyRequest(Optional.empty(),"오뜨","멍청이");

        // when
        final MemberModifyResponse response = memberService.patchMemberInfo(member, request);

        // then
        final MemberRequestResultType resultType = MemberRequestResultType.PATCH_MEMBER_INFO_SUCCESS;
        assertSoftly(softly -> {
            softly.assertThat(response.code()).isEqualTo(resultType.getCode());
            softly.assertThat(response.message()).isEqualTo(resultType.getMessage());
        });
    }
}
