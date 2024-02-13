package com.dnd.dotchi.domain.card.service;

import static com.dnd.dotchi.domain.card.dto.response.resultinfo.CardsByThemeRequestResultType.SUCCESS;

import com.dnd.dotchi.domain.card.dto.request.CardsByThemeRequest;
import com.dnd.dotchi.domain.card.dto.response.CardsByThemeResponse;
import com.dnd.dotchi.domain.card.entity.Card;
import com.dnd.dotchi.domain.card.repository.CardRepository;
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

        return CardsByThemeResponse.of(SUCCESS, cardsByTheme);
    }

}
