package com.dnd.dotchi.domain.blacklist.controller;

import com.dnd.dotchi.domain.blacklist.dto.request.BlockRequest;
import com.dnd.dotchi.domain.blacklist.dto.response.BlockResponse;
import com.dnd.dotchi.domain.blacklist.service.BlacklistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blacklists")
@RequiredArgsConstructor
public class BlacklistController {

    private final BlacklistService blacklistService;

    @PostMapping("{blacklistedId}")
    public ResponseEntity<BlockResponse> block(
            @PathVariable final Long blacklistedId,
            @Valid @ModelAttribute final BlockRequest request
    ) {
        final BlockResponse response = blacklistService.block(blacklistedId, request);
        return ResponseEntity.ok(response);
    }

}
