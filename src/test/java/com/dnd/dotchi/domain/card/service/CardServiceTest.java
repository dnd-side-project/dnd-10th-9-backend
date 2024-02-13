package com.dnd.dotchi.domain.card.service;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.dnd.dotchi.domain.card.entity.Card;
import com.dnd.dotchi.domain.card.entity.vo.CardSortType;
import com.dnd.dotchi.domain.card.repository.CardRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class CardServiceTest {

    @Autowired
    private CardRepository cardRepository;

    @Test
    @DisplayName("테마별 카드를 인기순으로 가져온다.")
    void getCardsByThemeWithHotSortType() {
        // given
        // data.sql

        // when
        final List<Card> result = cardRepository.findCardsByThemeWithFilteringAndPaging(
                2L,
                CardSortType.HOT,
                20L,
                20L
        );

        // then
        assertSoftly(softly -> {
            softly.assertThat(result).hasSize(5);
            softly.assertThat(result.get(0).getId()).isEqualTo(14);
            softly.assertThat(result.get(1).getId()).isEqualTo(18);
            softly.assertThat(result.get(2).getId()).isEqualTo(22);
            softly.assertThat(result.get(3).getId()).isEqualTo(26);
            softly.assertThat(result.get(4).getId()).isEqualTo(30);
        });
    }

    @Test
    @DisplayName("테마별 카드를 최신순으로 가져온다.")
    void getCardsByThemeWithLatestSortType() {
        // given
        // data.sql

        // when
        final List<Card> result = cardRepository.findCardsByThemeWithFilteringAndPaging(
                2L,
                CardSortType.LATEST,
                20L,
                20L
        );

        // then
        assertSoftly(softly -> {
            softly.assertThat(result).hasSize(5);
            softly.assertThat(result.get(0).getId()).isEqualTo(18);
            softly.assertThat(result.get(1).getId()).isEqualTo(14);
            softly.assertThat(result.get(2).getId()).isEqualTo(10);
            softly.assertThat(result.get(3).getId()).isEqualTo(6);
            softly.assertThat(result.get(4).getId()).isEqualTo(2);
        });
    }
}
