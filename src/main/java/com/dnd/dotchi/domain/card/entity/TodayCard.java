package com.dnd.dotchi.domain.card.entity;

import com.dnd.dotchi.domain.common.BaseEntity;
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
        name = "TODAY_CARD",
        indexes = {
                @Index(name = "idx_today_comment_count", columnList = "today_comment_count")
        }
)
public class TodayCard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false, unique = true)
    private Card card;

    @Column(name = "today_comment_count", nullable = false)
    @ColumnDefault("0")
    private Long todayCommentCount;

    @Builder
    public TodayCard(final Card card) {
        this.card = card;
        this.todayCommentCount = 0L;
    }

    public void increaseTodayCommentCountByOne() {
        this.todayCommentCount += 1L;
    }

}
