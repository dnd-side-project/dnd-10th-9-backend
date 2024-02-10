package com.dnd.dotchi.domain.blacklist.entity;

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
        name = "BLACKLIST",
        indexes = {
                @Index(name = "idx_blacklister_id", columnList = "blacklister_id"),
                @Index(name = "idx_blacklisted_id", columnList = "blacklisted_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_blacklister_id_blacklisted_id",
                        columnNames = {"blacklister_id", "blacklisted_id"}
                )
        }
)
public class BlackList extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blacklister_id", nullable = false)
    private Member blacklister;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blacklisted_id", nullable = false)
    private Member blacklisted;

    @Builder
    public BlackList(final Member blacklister, final Member blacklisted) {
        this.blacklister = blacklister;
        this.blacklisted = blacklisted;
    }

}
