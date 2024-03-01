package com.dnd.dotchi.global.redis;

import com.dnd.dotchi.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash("Member")
public class CacheMember {

    @Id
    private Long id;
    private Long sub;
    private String description;
    private String email;
    private String nickname;
    private String imageUrl;
    private Long cardCount;

    @Builder
    public CacheMember(
            final Long id,
            final Long sub,
            final String description,
            final String email,
            final String nickname,
            final String imageUrl,
            final Long cardCount
    ) {
        this.id = id;
        this.sub = sub;
        this.description = description;
        this.email = email;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.cardCount = cardCount;
    }

    public static CacheMember from(final Member member) {
        return CacheMember.builder()
                .id(member.getId())
                .sub(member.getSub())
                .description(member.getDescription())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .imageUrl(member.getImageUrl())
                .cardCount(member.getCardCount())
                .build();
    }

}
