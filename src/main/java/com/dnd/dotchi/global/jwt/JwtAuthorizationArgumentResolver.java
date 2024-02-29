package com.dnd.dotchi.global.jwt;

import com.dnd.dotchi.domain.member.service.MemberService;
import com.dnd.dotchi.global.redis.CacheMember;
import com.dnd.dotchi.global.redis.MemberRedisRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
@Component
public class JwtAuthorizationArgumentResolver implements HandlerMethodArgumentResolver {

    private final TokenProcessor tokenProcessor;
    private final MemberService memberService;
    private final MemberRedisRepository memberRedisRepository;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.withContainingClass(CacheMember.class)
                .hasParameterAnnotation(Auth.class);
    }

    @Override
    public CacheMember resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) throws JsonProcessingException {
        final String token = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        final String tokenWithoutType = tokenProcessor.resolveToken(token);
        tokenProcessor.validateToken(tokenWithoutType);
        final TokenPayload tokenPayload = tokenProcessor.parseToken(tokenWithoutType);

        final Long memberId = tokenPayload.memberId();
        return memberRedisRepository.findById(memberId)
                .orElseGet(() -> {
                    final CacheMember cacheMember = CacheMember.from(memberService.findById(memberId));
                    return memberRedisRepository.save(cacheMember);
                });
    }

}
