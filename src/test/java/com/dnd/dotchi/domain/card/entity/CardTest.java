package com.dnd.dotchi.domain.card.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CardTest {

    @Test
    @DisplayName("카드의 댓글 개수를 1개 증가시킵니다.")
    void increaseCommentCountByOne() {
        // given
        final Card card = Card.builder()
                .commentCount(0L)
                .build();

        // when
        card.increaseCommentCountByOne();

        // then
        assertThat(card.getCommentCount()).isEqualTo(1L);
    }

}
