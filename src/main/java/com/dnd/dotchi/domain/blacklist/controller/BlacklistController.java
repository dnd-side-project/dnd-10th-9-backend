package com.dnd.dotchi.domain.blacklist.controller;

import com.dnd.dotchi.domain.blacklist.dto.response.BlockResponse;
import com.dnd.dotchi.domain.blacklist.service.BlacklistService;
import com.dnd.dotchi.global.jwt.Auth;
import com.dnd.dotchi.global.redis.CacheMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blacklists")
@RequiredArgsConstructor
public class BlacklistController implements BlacklistControllerDocs {

    private final BlacklistService blacklistService;

    @PostMapping(value = "{blacklistedId}")
    public ResponseEntity<BlockResponse> block(
            @Auth final CacheMember member,
            @PathVariable("blacklistedId") final Long blacklistedId
    ) {
        final BlockResponse response = blacklistService.block(blacklistedId, member);

        if(response.code() == 1201) {
            return ResponseEntity.accepted().body(response);
        }
        return ResponseEntity.ok(response);
    }

}
