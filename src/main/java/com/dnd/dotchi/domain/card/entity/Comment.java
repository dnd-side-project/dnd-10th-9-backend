package com.dnd.dotchi.domain.card.entity;

import com.dnd.dotchi.domain.common.BaseEntity;
import com.dnd.dotchi.domain.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "COMMENT",
        indexes = {
                @Index(name = "idx_card_id", columnList = "card_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_member_id_card_id",
                        columnNames = {"member_id", "card_id"}
                )
        }
)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @Builder
    public Comment(final Member member, final Card card) {
        this.member = member;
        this.card = card;
    }

}
