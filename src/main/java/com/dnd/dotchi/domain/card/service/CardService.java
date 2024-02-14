package com.dnd.dotchi.domain.card.service;

import static com.dnd.dotchi.domain.card.dto.response.resultinfo.CardsRequestResultType.GET_CARDS_BY_THEME_SUCCESS;
import static com.dnd.dotchi.domain.card.dto.response.resultinfo.CardsRequestResultType.WRITE_COMMENT_ON_CARD_SUCCESS;

import com.dnd.dotchi.domain.card.dto.request.CardsByThemeRequest;
import com.dnd.dotchi.domain.card.dto.response.CardsByThemeResponse;
import com.dnd.dotchi.domain.card.dto.response.WriteCommentOnCardResponse;
import com.dnd.dotchi.domain.card.entity.Card;
import com.dnd.dotchi.domain.card.exception.CardExceptionType;
import com.dnd.dotchi.domain.card.repository.CardRepository;
import com.dnd.dotchi.global.exception.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CardService {

    private final CardRepository cardRepository;

    @Transactional(readOnly = true)
    public CardsByThemeResponse getCardsByTheme(final CardsByThemeRequest request) {
        List<Card> cardsByTheme = cardRepository.findCardsByThemeWithFilteringAndPaging(
                request.themeId(),
                request.cardSortType(),
                request.lastCardId(),
                request.lastCardCommentCount()
        );

        return CardsByThemeResponse.of(GET_CARDS_BY_THEME_SUCCESS, cardsByTheme);
    }

    public WriteCommentOnCardResponse writeCommentOnCard(final Long cardId) {
        final Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException(CardExceptionType.NOT_FOUND_CARD));

        card.increaseCommentCountByOne();
        return WriteCommentOnCardResponse.from(WRITE_COMMENT_ON_CARD_SUCCESS);
    }

}
