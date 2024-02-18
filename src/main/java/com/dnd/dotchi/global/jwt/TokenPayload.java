package com.dnd.dotchi.global.jwt;

public record TokenPayload(
        Long memberId,
        Long iat,
        Long exp
) {
}
