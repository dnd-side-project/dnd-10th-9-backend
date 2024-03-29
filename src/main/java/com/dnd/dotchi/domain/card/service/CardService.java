package com.dnd.dotchi.domain.card.service;

import static com.dnd.dotchi.domain.card.dto.response.resultinfo.CardsRequestResultType.GET_CARDS_BY_THEME_SUCCESS;
import static com.dnd.dotchi.domain.card.dto.response.resultinfo.CardsRequestResultType.WRITE_CARDS_SUCCESS;
import static com.dnd.dotchi.domain.card.dto.response.resultinfo.CardsRequestResultType.WRITE_COMMENT_ON_CARD_SUCCESS;

import com.dnd.dotchi.domain.blacklist.entity.BlackList;
import com.dnd.dotchi.domain.blacklist.repository.BlackListRepository;
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
import com.dnd.dotchi.domain.member.exception.MemberExceptionType;
import com.dnd.dotchi.domain.member.repository.MemberRepository;
import com.dnd.dotchi.global.exception.BadRequestException;
import com.dnd.dotchi.global.exception.NotFoundException;
import com.dnd.dotchi.global.exception.RetryLimitExceededException;
import com.dnd.dotchi.global.redis.CacheMember;
import com.dnd.dotchi.infra.image.S3FileUploader;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
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
    private final BlackListRepository blackListRepository;
    private final MemberRepository memberRepository;
    private final S3FileUploader s3FileUploader;

    public CardsWriteResponse write(final CardsWriteRequest request, final CacheMember cacheMember) {
        final String fileFullPath = s3FileUploader.upload(request.image());
        final Member member = getMemberByCacheMember(cacheMember);

        final Card card = Card.builder()
                .member(member)
                .theme(getTheme(request))
                .imageUrl(fileFullPath)
                .backName(request.backName())
                .backMood(request.backMood())
                .backContent(request.backContent())
                .build();
        member.increaseCardCountByOne();
        cardRepository.save(card);

        return CardsWriteResponse.from(WRITE_CARDS_SUCCESS);
    }

    private Theme getTheme(CardsWriteRequest cardsWriteRequest) {
        return themeJpaRepository.findById(cardsWriteRequest.themeId())
                .orElseThrow(() -> new NotFoundException(ThemeExceptionType.NOT_FOUND_THEME));
    }

    @Transactional(readOnly = true)
    public CardsByThemeResponse getCardsByTheme(final CacheMember member, final CardsByThemeRequest request) {
        final List<Card> cardsByTheme = cardRepository.findCardsByThemeWithFilteringAndPaging(
                request.themeId(),
                request.cardSortType(),
                request.lastCardId(),
                request.lastCardCommentCount(),
                getIdsRelatedToBlocking(member)
        );

        return CardsByThemeResponse.of(GET_CARDS_BY_THEME_SUCCESS, cardsByTheme);
    }

    @Retryable(
            retryFor = ObjectOptimisticLockingFailureException.class,
            backoff = @Backoff(delay = 100)
    )
    public WriteCommentOnCardResponse writeCommentOnCard(final CacheMember member, final Long cardId) {
        commentRepository.findByMemberIdAndCardId(member.getId(), cardId)
                .ifPresent(comment -> {throw new BadRequestException(CardExceptionType.MY_COMMENT_ALREADY_EXIST);});
        final Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException(CardExceptionType.NOT_FOUND_CARD));

        card.increaseCommentCountByOne();
        todayCardIncreaseCommentCount(card);
        saveComment(member, card);

        return WriteCommentOnCardResponse.from(WRITE_COMMENT_ON_CARD_SUCCESS);
    }

    private void saveComment(final CacheMember member, final Card card) {
        final Comment comment = Comment.builder()
                .member(getMemberByCacheMember(member))
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
            final CacheMember member,
            final Long cardId
    ) {
        throw new RetryLimitExceededException(CardExceptionType.WRITE_COMMENT_ON_CARD_FAILURE);
    }

    @Recover
    public WriteCommentOnCardResponse recoverWriteCommentOnCard(
            final NotFoundException e,
            final CacheMember member,
            final Long cardId
    ) {
        throw e;
    }

    @Recover
    public WriteCommentOnCardResponse recoverWriteCommentOnCard(
            final BadRequestException e,
            final CacheMember member,
            final Long cardId
    ) {
        throw e;
    }

    @Transactional(readOnly = true)
    public CardsAllResponse getCardAll(final CacheMember member, final CardsAllRequest request) {
        final List<Card> cards = cardRepository.findCardsAllWithFilteringAndPaging(
                request.cardSortType(),
                request.lastCardId(),
                request.lastCardCommentCount(),
                getIdsRelatedToBlocking(member)
        );

        return CardsAllResponse.of(CardsRequestResultType.GET_CARDS_ALL_SUCCESS, cards);
    }

    public DeleteCardResponse delete(final CacheMember member, final Long cardId) {
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
    public GetCommentOnCardResponse getCommentOnCard(final CacheMember member, final Long cardId) {
        final Card card = findById(cardId);

        final List<Member> authors
            = commentRepository.findTop3LatestCommentsFilter(getIdsRelatedToBlocking(member), cardId).stream()
            .map(Comment::getMember)
            .toList();

        return GetCommentOnCardResponse.of(
            card,
            authors,
            hasComment(member.getId(), cardId),
            CardsRequestResultType.GET_COMMENT_ON_CARD_SUCCESS
        );
    }

    private List<Long> getIdsRelatedToBlocking(final CacheMember member) {
        final List<Long> blacklistedIds = blackListRepository.findByBlacklisterId(member.getId()).stream()
                .map(BlackList::getBlacklisted)
                .map(Member::getId)
                .toList();
        final List<Long> blacklisterIds = blackListRepository.findByBlacklistedId(member.getId()).stream()
                .map(BlackList::getBlacklister)
                .map(Member::getId)
                .toList();
        return Stream.of(blacklisterIds, blacklistedIds)
                .flatMap(Collection::stream)
                .toList();
    }

    private Boolean hasComment(final Long memberId, final Long cardId) {
        return commentRepository.findByMemberIdAndCardId(memberId, cardId).isPresent();
    }

    private Card findById(final Long cardId) {
        return cardRepository.findById(cardId)
            .orElseThrow(() -> new NotFoundException(CardExceptionType.NOT_FOUND_CARD));
    }

    @Transactional(readOnly = true)
    public HomePageResponse home(final CacheMember member) {
        final List<TodayCard> todayCards = todayCardRepository.findTodayCards(getIdsRelatedToBlocking(member));
        final List<Card> recentCards = cardRepository.findTop5ByOrderByIdDesc(getIdsRelatedToBlocking(member));
        final List<Card> recentCardsByThemes = cardRepository.findRecentCardByThemes();

        return HomePageResponse.of(
            CardsRequestResultType.GET_MAIN_HOME_SUCCESS,
            todayCards,
            recentCards,
            recentCardsByThemes
        );
    }

    private Member getMemberByCacheMember(final CacheMember cacheMember) {
        return memberRepository.findById(cacheMember.getId())
                .orElseThrow(() -> new NotFoundException(MemberExceptionType.NOT_FOUND_MEMBER));
    }

}
