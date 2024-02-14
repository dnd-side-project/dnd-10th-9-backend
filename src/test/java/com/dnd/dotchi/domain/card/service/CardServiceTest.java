package com.dnd.dotchi.domain.card.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.dnd.dotchi.domain.card.dto.response.WriteCommentOnCardResponse;
import com.dnd.dotchi.domain.card.dto.response.resultinfo.CardsRequestResultType;
import com.dnd.dotchi.domain.card.entity.Card;
import com.dnd.dotchi.domain.card.exception.CardExceptionType;
import com.dnd.dotchi.domain.card.repository.CardRepository;
import com.dnd.dotchi.global.exception.NotFoundException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class CardServiceTest {

    @Autowired
    CardService cardService;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("카드에 댓글을 작성한다.")
    void writeCommentOnCard() {
        // given
        // data.sql
        final long cardId = 1L;

        // when
        final WriteCommentOnCardResponse result = cardService.writeCommentOnCard(cardId);
        em.flush();
        em.clear();

        // then
        final Card card = cardRepository.findById(cardId).get();
        final CardsRequestResultType resultType = CardsRequestResultType.WRITE_COMMENT_ON_CARD_SUCCESS;

        assertSoftly(softly -> {
            softly.assertThat(card.getCommentCount()).isEqualTo(32L);
            softly.assertThat(result.code()).isEqualTo(resultType.getCode());
            softly.assertThat(result.message()).isEqualTo(resultType.getMessage());
        });
    }

    @Test
    @DisplayName("카드에 댓글 작성시, 존재하지 않는 카드 ID를 전달할 때 NotFound 예외가 발생한다.")
    void writeCommentOnCardNotFoundException() {
        // given
        // data.sql
        final long cardId = cardRepository.findAll().size() + 1L;

        // when, then
        assertThatThrownBy(() -> cardService.writeCommentOnCard(cardId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(CardExceptionType.NOT_FOUND_CARD.getMessage());
    }
}
