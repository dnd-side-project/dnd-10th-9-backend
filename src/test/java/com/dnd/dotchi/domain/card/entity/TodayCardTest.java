package com.dnd.dotchi.domain.card.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TodayCardTest {

    @Test
    @DisplayName("오늘의 카드의 댓글 개수를 1개 증가시킵니다.")
    void increaseCommentCountByOne() {
        // given
        final Card card = Card.builder()
                .build();

        final TodayCard todayCard = TodayCard.builder().card(card).build();

        // when
        todayCard.increaseTodayCommentCountByOne();

        // then
        assertThat(todayCard.getTodayCommentCount()).isOne();
    }

}
