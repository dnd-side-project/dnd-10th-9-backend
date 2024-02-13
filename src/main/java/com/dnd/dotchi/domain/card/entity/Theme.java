package com.dnd.dotchi.domain.card.entity;

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

@Entity
@Getter
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "THEME")
public class Theme extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "theme_concept", nullable = false, length = 20)
    private String themeConcept;

    @Column(name = "card_back_last_content", nullable = false)
    private String cardBackLastContent;

    @Builder
    public Theme(final String themeConcept, final String cardBackLastContent) {
        this.themeConcept = themeConcept;
        this.cardBackLastContent = cardBackLastContent;
    }

}
