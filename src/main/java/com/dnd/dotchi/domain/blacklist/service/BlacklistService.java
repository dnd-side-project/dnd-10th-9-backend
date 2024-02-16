package com.dnd.dotchi.domain.blacklist.service;

import com.dnd.dotchi.domain.blacklist.dto.request.BlockRequest;
import com.dnd.dotchi.domain.blacklist.dto.response.BlockResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class BlacklistService {

    public BlockResponse block(final Long blacklistedId, final BlockRequest request) {
        return null;
    }

}
