package com.dnd.dotchi.domain.card.service;

import static com.dnd.dotchi.domain.card.dto.response.resultinfo.CardsRequestResultType.GET_CARDS_BY_THEME_SUCCESS;
import static com.dnd.dotchi.domain.card.dto.response.resultinfo.CardsRequestResultType.WRITE_CARDS_SUCCESS;
import static com.dnd.dotchi.domain.card.dto.response.resultinfo.CardsRequestResultType.WRITE_COMMENT_ON_CARD_SUCCESS;

import com.dnd.dotchi.domain.card.dto.request.CardsAllRequest;
import com.dnd.dotchi.domain.card.dto.request.CardsByThemeRequest;
import com.dnd.dotchi.domain.card.dto.request.CardsWriteRequest;
import com.dnd.dotchi.domain.card.dto.response.CardsAllResponse;
import com.dnd.dotchi.domain.card.dto.response.CardsByThemeResponse;
import com.dnd.dotchi.domain.card.dto.response.CardsWriteResponse;
import com.dnd.dotchi.domain.card.dto.response.DeleteCardResponse;
import com.dnd.dotchi.domain.card.dto.response.GetCommentOnCardResponse;
import com.dnd.dotchi.domain.card.dto.response.HomePageResponse;
import com.dnd.dotchi.domain.card.dto.response.WriteCommentOnCardResponse;
import com.dnd.dotchi.domain.card.dto.response.resultinfo.CardsRequestResultType;
import com.dnd.dotchi.domain.card.entity.Card;
import com.dnd.dotchi.domain.card.entity.Comment;
import com.dnd.dotchi.domain.card.entity.Theme;
import com.dnd.dotchi.domain.card.entity.TodayCard;
import com.dnd.dotchi.domain.card.exception.CardExceptionType;
import com.dnd.dotchi.domain.card.exception.ThemeExceptionType;
import com.dnd.dotchi.domain.card.repository.CardRepository;
import com.dnd.dotchi.domain.card.repository.CommentRepository;
import com.dnd.dotchi.domain.card.repository.ThemeRepository;
import com.dnd.dotchi.domain.card.repository.TodayCardRepository;
import com.dnd.dotchi.domain.member.entity.Member;
import com.dnd.dotchi.global.exception.BadRequestException;
import com.dnd.dotchi.global.exception.NotFoundException;
import com.dnd.dotchi.global.exception.RetryLimitExceededException;
import com.dnd.dotchi.infra.image.ImageUploader;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Objects;
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
    private final CommentRepository commentRepository;
    private final ImageUploader imageUploader;

    public CardsWriteResponse write(final CardsWriteRequest request, final Member member) {
        final String fileFullPath = imageUploader.upload(request.image());

        final Card cardEntity = Card.builder()
                .member(member)
                .theme(getTheme(request))
                .imageUrl(fileFullPath)
                .backName(request.backName())
                .backMood(request.backMood())
                .backContent(request.backContent())
                .build();
        cardRepository.save(cardEntity);

        return CardsWriteResponse.from(WRITE_CARDS_SUCCESS);
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
    public WriteCommentOnCardResponse writeCommentOnCard(final Member member, final Long cardId) {
        commentRepository.findByMemberIdAndCardId(member.getId(), cardId)
                .ifPresent(comment -> {throw new BadRequestException(CardExceptionType.MY_COMMENT_ALREADY_EXIST);});
        final Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException(CardExceptionType.NOT_FOUND_CARD));

        card.increaseCommentCountByOne();
        todayCardIncreaseCommentCount(card);
        saveComment(member, card);

        return WriteCommentOnCardResponse.from(WRITE_COMMENT_ON_CARD_SUCCESS);
    }

    private void saveComment(final Member member, final Card card) {
        final Comment comment = Comment.builder()
                .member(member)
                .card(card)
                .build();
        commentRepository.save(comment);
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
            final Member member,
            final Long cardId
    ) {
        throw new RetryLimitExceededException(CardExceptionType.WRITE_COMMENT_ON_CARD_FAILURE);
    }

    @Recover
    public WriteCommentOnCardResponse recoverWriteCommentOnCard(
            final NotFoundException e,
            final Member member,
            final Long cardId
    ) {
        throw e;
    }

    @Recover
    public WriteCommentOnCardResponse recoverWriteCommentOnCard(
            final BadRequestException e,
            final Member member,
            final Long cardId
    ) {
        throw e;
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

    public DeleteCardResponse delete(final Member member, final Long cardId) {
        final Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException(CardExceptionType.NOT_FOUND_CARD));

        if (!Objects.equals(member.getId(), card.getMember().getId())) {
            throw new BadRequestException(CardExceptionType.NOT_CARD_WRITER);
        }

        cardRepository.deleteById(cardId);
        return DeleteCardResponse.from(CardsRequestResultType.DELETE_CARD_SUCCESS);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteAllTodayCardTableAtMidnight() {
        todayCardRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    public GetCommentOnCardResponse getCommentOnCard(final Long cardId) {
        final Card card = cardRepository.findById(cardId)
            .orElseThrow(() -> new NotFoundException(CardExceptionType.NOT_FOUND_CARD));

        final List<Member> authors
            = commentRepository.findTop3ByCardIdOrderByIdDesc(cardId).stream()
            .map(Comment::getMember)
            .toList();

        return GetCommentOnCardResponse.of(
            card,
            authors,
            CardsRequestResultType.GET_COMMENT_ON_CARD_SUCCESS
        );
    }

    @Transactional(readOnly = true)
    public HomePageResponse home() {
        final List<TodayCard> todayCards = todayCardRepository.findTop3ByOrderByTodayCommentCountDesc();
        final List<Card> recentCards = cardRepository.findTop5ByOrderByIdDesc();
        final List<Card> recentCardsByThemes = cardRepository.findRecentCardByThemes();

        return HomePageResponse.of(
            CardsRequestResultType.GET_MAIN_HOME_SUCCESS,
            todayCards,
            recentCards,
            recentCardsByThemes
        );
    }

}
