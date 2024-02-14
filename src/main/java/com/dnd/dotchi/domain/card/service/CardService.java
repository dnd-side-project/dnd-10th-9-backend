package com.dnd.dotchi.domain.card.service;

import static com.dnd.dotchi.domain.card.dto.response.resultinfo.CardsRequestResultType.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dnd.dotchi.domain.card.dto.request.CardsByThemeRequest;
import com.dnd.dotchi.domain.card.dto.request.CardsWriteRequest;
import com.dnd.dotchi.domain.card.dto.response.CardsByThemeResponse;
import com.dnd.dotchi.domain.card.dto.response.CardsWriteResponse;
import com.dnd.dotchi.domain.card.entity.Card;
import com.dnd.dotchi.domain.card.entity.Theme;
import com.dnd.dotchi.domain.card.exception.ThemeExceptionType;
import com.dnd.dotchi.domain.card.repository.CardRepository;
import com.dnd.dotchi.domain.card.repository.ThemeRepository;
import com.dnd.dotchi.domain.member.entity.Member;
import com.dnd.dotchi.domain.member.exception.MemberExceptionType;
import com.dnd.dotchi.domain.member.repository.MemberRepository;
import com.dnd.dotchi.global.exception.NotFoundException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class CardService {

	private final CardRepository cardRepository;
	private final ThemeRepository themeJpaRepository;
	private final MemberRepository memberJpaRepository;

	public CardsWriteResponse write(final CardsWriteRequest request) {
		final Card cardEntity = Card.builder()
				.member(getMember(request))
				.theme(getTheme(request))
				.imageUrl(request.imageUrl())
				.backName(request.backName())
				.backMood(request.backMood())
				.backContent(request.backContent())
				.build();
		cardRepository.save(cardEntity);

		return CardsWriteResponse.of(WRITE_CARDS_SUCCESS);
	}

	private Member getMember(CardsWriteRequest cardsWriteRequest) {
		return memberJpaRepository.findById(cardsWriteRequest.memberId())
			.orElseThrow(() -> new NotFoundException(MemberExceptionType.NOT_FOUND_MEMBER));
	}

	private Theme getTheme(CardsWriteRequest cardsWriteRequest) {
		return themeJpaRepository.findById(cardsWriteRequest.themeId())
			.orElseThrow(() -> new NotFoundException(ThemeExceptionType.NOT_FOUND_THEME));
	}

    @Transactional(readOnly = true)
    public CardsByThemeResponse getCardsByTheme(final @Valid CardsByThemeRequest request) {
        List<Card> cardsByTheme = cardRepository.findCardsByThemeWithFilteringAndPaging(
                request.themeId(),
                request.cardSortType(),
                request.lastCardId(),
                request.lastCardCommentCount()
        );

        return CardsByThemeResponse.of(GET_CARDS_BY_THEME_SUCCESS, cardsByTheme);
    }

}
