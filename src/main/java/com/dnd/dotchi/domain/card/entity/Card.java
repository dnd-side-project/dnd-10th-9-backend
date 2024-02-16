package com.dnd.dotchi.domain.card.entity;

import com.dnd.dotchi.domain.common.BaseEntity;
import com.dnd.dotchi.domain.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "CARD",
        indexes = {
                @Index(name = "idx_member_id", columnList = "member_id"),
                @Index(name = "idx_theme_id_id", columnList = "theme_id, id"),
                @Index(name = "idx_theme_id_comment_count", columnList = "theme_id, comment_count")
        }
)
public class Card extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id", nullable = false)
    private Theme theme;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "back_name", nullable = false, length = 7)
    private String backName;

    @Column(name = "back_mood", nullable = false, length = 15)
    private String backMood;

    @Column(name = "back_content", length = 20)
    private String backContent;

    @Column(name = "comment_count")
    @ColumnDefault("0")
    private Long commentCount;

    @Version
    @ColumnDefault("0")
    private Long version;

    @Builder
    public Card(
            final Member member,
            final Theme theme,
            final String imageUrl,
            final String backName,
            final String backMood,
            final String backContent
    ) {
        this.member = member;
        this.theme = theme;
        this.imageUrl = imageUrl;
        this.backName = backName;
        this.backMood = backMood;
        this.backContent = backContent;
        this.commentCount = 0L;
    }

    public void increaseCommentCountByOne() {
        this.commentCount += 1;
    }

}
