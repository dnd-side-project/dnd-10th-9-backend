package com.dnd.dotchi.domain.card.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.dnd.dotchi.domain.card.entity.Card;
import com.dnd.dotchi.domain.card.entity.TodayCard;
import com.dnd.dotchi.global.config.JpaConfig;
import com.dnd.dotchi.global.config.QuerydslConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import({JpaConfig.class, QuerydslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class TodayCardRepositoryTest {

    @Autowired
    TodayCardRepository todayCardRepository;

    @Autowired
    CardRepository cardRepository;

    @Test
    @DisplayName("카드 ID로 TodayCard 엔티티를 조회한다.")
    void findByCardId() {
        // given
        final long cardId = todayCardRepository.count() + 1L;
        final Card card = cardRepository.findById(cardId).get();

        final TodayCard todayCard = TodayCard.builder().card(card).build();
        todayCardRepository.save(todayCard);

        // when
        final TodayCard resultTodayCard = todayCardRepository.findByCardId(cardId).get();

        // then
        assertThat(resultTodayCard.getCard().getId()).isEqualTo(cardId);
    }

}
