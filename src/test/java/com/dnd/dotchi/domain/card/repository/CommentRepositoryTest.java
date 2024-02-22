package com.dnd.dotchi.domain.card.repository;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.dnd.dotchi.domain.card.entity.Comment;
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
class CommentRepositoryTest {

	@Autowired
	CommentRepository commentRepository;

	@Test
	@DisplayName("차단한 회원을 제외한 댓글을 조회한다.")
	void getCommentsOnCardTest1() {
		// given
		// data-test.sql
		final Long cardId = 2L;

		// when
		final List<Comment> result = commentRepository.findTop3LatestCommentsFilter(List.of(1L), cardId);

		// then
		assertSoftly(softly -> {
			softly.assertThat(result).hasSize(3);
			softly.assertThat(result.get(0).getMember().getId()).isEqualTo(5L);
			softly.assertThat(result.get(1).getMember().getId()).isEqualTo(3L);
			softly.assertThat(result.get(2).getMember().getId()).isEqualTo(4L);
		});
	}

}
