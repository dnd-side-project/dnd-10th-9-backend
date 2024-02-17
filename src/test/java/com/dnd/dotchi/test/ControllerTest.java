package com.dnd.dotchi.test;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.dnd.dotchi.domain.member.entity.Member;
import com.dnd.dotchi.domain.member.service.MemberService;
import com.dnd.dotchi.global.jwt.TokenPayload;
import com.dnd.dotchi.global.jwt.TokenProcessor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

public class ControllerTest {

    @MockBean
    protected TokenProcessor tokenProcessor;

    @MockBean
    protected MemberService memberService;

    protected static final String BEARER_TOKEN = "Bearer token";

    protected void mockingAuthArgumentResolver() throws Exception {
        TokenPayload tokenPayload = new TokenPayload(1L, 1L, 1L);
        given(tokenProcessor.resolveToken(anyString())).willReturn("token");
        given(tokenProcessor.parseToken(anyString())).willReturn(tokenPayload);
        final Member member = Member.builder()
                .sub(1L)
                .nickname("오뜨")
                .imageUrl("/image.jpg")
                .email("email")
                .description("안녕")
                .build();
        ReflectionTestUtils.setField(member, "id", 1L);
        given(memberService.findById(anyLong())).willReturn(member);
    }

}
