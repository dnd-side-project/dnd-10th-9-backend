package com.dnd.dotchi.domain.member.entity;

import com.dnd.dotchi.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "MEMBER")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long sub;

    @Column(length = 40)
    private String description;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "card_count")
    @ColumnDefault("0")
    private Long cardCount;

    @Builder
    public Member(
            final Long sub,
            final String description,
            final String email,
            final String nickname,
            final String imageUrl,
            final Long cardCount
    ) {
        this.sub = sub;
        this.description = description;
        this.email = email;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.cardCount = cardCount;
    }

}
