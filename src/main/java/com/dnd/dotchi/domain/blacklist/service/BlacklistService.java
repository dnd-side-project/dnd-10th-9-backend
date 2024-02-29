package com.dnd.dotchi.domain.blacklist.service;

import com.dnd.dotchi.global.redis.CacheMember;
import java.util.Optional;

import com.dnd.dotchi.domain.blacklist.dto.response.BlockResponse;
import com.dnd.dotchi.domain.blacklist.dto.response.resulltinfo.BlockRequestResultType;
import com.dnd.dotchi.domain.blacklist.entity.BlackList;
import com.dnd.dotchi.domain.blacklist.repository.BlackListRepository;
import com.dnd.dotchi.domain.member.entity.Member;
import com.dnd.dotchi.domain.member.exception.MemberExceptionType;
import com.dnd.dotchi.domain.member.repository.MemberRepository;
import com.dnd.dotchi.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class BlacklistService {

    private final BlackListRepository blackListRepository;
    private final MemberRepository memberRepository;

    public BlockResponse block(final Long blacklistedId, final CacheMember blacklister) {
        if (isAlready(blacklister.getId(), blacklistedId)) {
            return BlockResponse.from(BlockRequestResultType.ALREADY_BLOCK);
        }

        final BlackList blackList = BlackList.builder()
            .blacklister(getMember(blacklister.getId()))
            .blacklisted(getMember(blacklistedId))
            .build();

        blackListRepository.save(blackList);
        return BlockResponse.from(BlockRequestResultType.BLOCK_SUCCESS);
    }

    private Member getMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(MemberExceptionType.NOT_FOUND_MEMBER));
    }

    private boolean isAlready(final Long blacklisterId, final Long blacklistedId) {
        final Optional<BlackList> alreadyBlock
            = blackListRepository.findByBlacklisterIdAndBlacklistedId(blacklisterId, blacklistedId);

        return alreadyBlock.isPresent();
    }

}
