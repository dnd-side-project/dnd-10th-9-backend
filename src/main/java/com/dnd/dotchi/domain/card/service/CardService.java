package com.dnd.dotchi.domain.card.service;

import static com.dnd.dotchi.domain.card.dto.response.resultinfo.CardsRequestResultType.GET_CARDS_BY_THEME_SUCCESS;
import static com.dnd.dotchi.domain.card.dto.response.resultinfo.CardsRequestResultType.WRITE_CARDS_SUCCESS;
import static com.dnd.dotchi.domain.card.dto.response.resultinfo.CardsRequestResultType.WRITE_COMMENT_ON_CARD_SUCCESS;

import com.dnd.dotchi.domain.card.dto.request.CardsAllRequest;
import com.dnd.dotchi.domain.card.dto.request.CardsByThemeRequest;
import com.dnd.dotchi.domain.card.dto.response.CardsAllResponse;
import com.dnd.dotchi.domain.card.dto.request.CardsWriteRequest;
import com.dnd.dotchi.domain.card.dto.response.CardsByThemeResponse;
import com.dnd.dotchi.domain.card.dto.response.CardsWriteResponse;
import com.dnd.dotchi.domain.card.dto.response.WriteCommentOnCardResponse;
import com.dnd.dotchi.domain.card.dto.response.resultinfo.CardsRequestResultType;
import com.dnd.dotchi.domain.card.entity.Card;
import com.dnd.dotchi.domain.card.entity.Theme;
import com.dnd.dotchi.domain.card.entity.TodayCard;
import com.dnd.dotchi.domain.card.exception.CardExceptionType;
import com.dnd.dotchi.global.exception.RetryLimitExceededException;
import com.dnd.dotchi.domain.card.exception.ThemeExceptionType;
import com.dnd.dotchi.domain.card.repository.CardRepository;
import com.dnd.dotchi.domain.card.repository.ThemeRepository;
import com.dnd.dotchi.domain.card.repository.TodayCardRepository;
import com.dnd.dotchi.domain.member.entity.Member;
import com.dnd.dotchi.domain.member.exception.MemberExceptionType;
import com.dnd.dotchi.domain.member.repository.MemberRepository;
import com.dnd.dotchi.global.exception.NotFoundException;
import com.dnd.dotchi.infra.image.ImageUploader;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CardService {

    private final CardRepository cardRepository;
    private final TodayCardRepository todayCardRepository;
    private final ThemeRepository themeJpaRepository;
    private final MemberRepository memberJpaRepository;
    private final ImageUploader imageUploader;

    public CardsWriteResponse write(final CardsWriteRequest request) {
        final String fileFullPath = imageUploader.upload(request.image());

        final Card cardEntity = Card.builder()
                .member(getMember(request))
                .theme(getTheme(request))
                .imageUrl(fileFullPath)
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
        final List<Card> cardsByTheme = cardRepository.findCardsByThemeWithFilteringAndPaging(
                request.themeId(),
                request.cardSortType(),
                request.lastCardId(),
                request.lastCardCommentCount()
        );

        return CardsByThemeResponse.of(GET_CARDS_BY_THEME_SUCCESS, cardsByTheme);
    }

    @Retryable(
            retryFor = ObjectOptimisticLockingFailureException.class,
            backoff = @Backoff(delay = 100)
    )
    public WriteCommentOnCardResponse writeCommentOnCard(final Long cardId) {
        final Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException(CardExceptionType.NOT_FOUND_CARD));

        card.increaseCommentCountByOne();
        todayCardIncreaseCommentCount(card);

        return WriteCommentOnCardResponse.from(WRITE_COMMENT_ON_CARD_SUCCESS);
    }

    private void todayCardIncreaseCommentCount(final Card card) {
        final Optional<TodayCard> todayCard = todayCardRepository.findByCardId(card.getId());
        if (todayCard.isEmpty()) {
            final TodayCard todayCardToSave = TodayCard.builder().card(card).build();
            todayCardToSave.increaseTodayCommentCountByOne();
            todayCardRepository.save(todayCardToSave);
            return;
        }

        todayCard.get().increaseTodayCommentCountByOne();
    }

    @Recover
    public WriteCommentOnCardResponse recoverWriteCommentOnCard(
            final OptimisticLockingFailureException e,
            final Long cardId
    ) {
        throw new RetryLimitExceededException(CardExceptionType.WRITE_COMMENT_ON_CARD_FAILURE);
    }

    @Recover
    public WriteCommentOnCardResponse recoverWriteCommentOnCard(
            final NotFoundException e,
            final Long cardId
    ) {
        throw e;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteAllTodayCardTableAtMidnight() {
        todayCardRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    public CardsAllResponse getCardAll(final CardsAllRequest request) {
        final List<Card> cards = cardRepository.findCardsAllWithFilteringAndPaging(
            request.cardSortType(),
            request.lastCardId(),
            request.lastCardCommentCount()
        );

        return CardsAllResponse.of(CardsRequestResultType.GET_CARDS_ALL_SUCCESS, cards);
    }

}
