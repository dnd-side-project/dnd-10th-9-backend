package com.dnd.dotchi.domain.card.repository;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.dnd.dotchi.domain.card.entity.Card;
import com.dnd.dotchi.domain.card.entity.vo.CardSortType;
import com.dnd.dotchi.global.config.JpaConfig;
import com.dnd.dotchi.global.config.QuerydslConfig;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import({JpaConfig.class, QuerydslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class CardRepositoryTest {

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

	@Test
	@DisplayName("전체 카드를 최신순으로 가져온다.")
	void getCardsAllWithLatestSortType() {
		// given
		// data.sql

		// when
		final List<Card> result = cardRepository.findCardsAllWithFilteringAndPaging(
			CardSortType.LATEST,
			15L,
			20L
		);

		// then
		assertSoftly(softly -> {
			softly.assertThat(result).hasSize(10);
			softly.assertThat(result.get(0).getId()).isEqualTo(14);
			softly.assertThat(result.get(1).getId()).isEqualTo(13);
			softly.assertThat(result.get(2).getId()).isEqualTo(12);
			softly.assertThat(result.get(3).getId()).isEqualTo(11);
			softly.assertThat(result.get(4).getId()).isEqualTo(10);
		});
	}

	@Test
	@DisplayName("전체 카드를 인기순으로 가져온다.")
	void getCardsAllWithHotSortType() {
		// given
		// data.sql

		// when
		final List<Card> result = cardRepository.findCardsAllWithFilteringAndPaging(
			CardSortType.HOT,
			15L,
			20L
		);

		// then
		assertSoftly(softly -> {
			softly.assertThat(result).hasSize(10);
			softly.assertThat(result.get(0).getId()).isEqualTo(13);
			softly.assertThat(result.get(1).getId()).isEqualTo(14);
			softly.assertThat(result.get(2).getId()).isEqualTo(15);
			softly.assertThat(result.get(3).getId()).isEqualTo(16);
			softly.assertThat(result.get(4).getId()).isEqualTo(17);
		});
	}

	@Test
	@DisplayName("특정 회원이 작성한 카드를 최신순으로 가져온다.")
	void getCardsByMemberWithLatestSortType() {
		// given
		// data.sql
		final long memberId = 1L;
		final long lastCardId = 25L;

		// when
		final List<Card> recent = cardRepository.findCardsByMemberWithFilteringAndPaging(memberId, lastCardId);

		// then
		assertSoftly(softly -> {
			softly.assertThat(recent).hasSize(10);
			softly.assertThat(recent.get(0).getId()).isEqualTo(23);
			softly.assertThat(recent.get(1).getId()).isEqualTo(21);
			softly.assertThat(recent.get(2).getId()).isEqualTo(19);
			softly.assertThat(recent.get(3).getId()).isEqualTo(17);
			softly.assertThat(recent.get(4).getId()).isEqualTo(15);
			softly.assertThat(recent.get(5).getId()).isEqualTo(13);
			softly.assertThat(recent.get(6).getId()).isEqualTo(11);
			softly.assertThat(recent.get(7).getId()).isEqualTo(9);
			softly.assertThat(recent.get(8).getId()).isEqualTo(7);
			softly.assertThat(recent.get(9).getId()).isEqualTo(5);
		});
	}

	@Test
	@DisplayName("테마별 최신 카드를 가져온다.")
	void getRecentCardByThemes() {
		// given
		// data.sql

		// when
		final List<Card> recent = cardRepository.findRecentCardByThemes();

		// then
		assertSoftly(softly -> {
			softly.assertThat(recent).hasSize(4);
			softly.assertThat(recent.get(0).getId()).isEqualTo(29);
			softly.assertThat(recent.get(1).getId()).isEqualTo(30);
			softly.assertThat(recent.get(2).getId()).isEqualTo(27);
			softly.assertThat(recent.get(3).getId()).isEqualTo(28);
			softly.assertThat(recent.get(0).getTheme().getId()).isEqualTo(1);
			softly.assertThat(recent.get(1).getTheme().getId()).isEqualTo(2);
			softly.assertThat(recent.get(2).getTheme().getId()).isEqualTo(3);
			softly.assertThat(recent.get(3).getTheme().getId()).isEqualTo(4);
		});
	}

}
