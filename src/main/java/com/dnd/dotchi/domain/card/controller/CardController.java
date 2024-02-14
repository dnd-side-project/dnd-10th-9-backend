package com.dnd.dotchi.domain.card.controller;

import com.dnd.dotchi.domain.card.dto.request.CardsByThemeRequest;
import com.dnd.dotchi.domain.card.dto.response.CardsByThemeResponse;
import com.dnd.dotchi.domain.card.dto.response.WriteCommentOnCardResponse;
import com.dnd.dotchi.domain.card.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/cards")
@RestController
public class CardController implements CardControllerDocs {

    private final CardService cardService;

    @GetMapping("/theme")
    public ResponseEntity<CardsByThemeResponse> getCardsByTheme(
            @Valid @ModelAttribute CardsByThemeRequest request
    ) {
        final CardsByThemeResponse response = cardService.getCardsByTheme(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{cardId}/comments")
    public ResponseEntity<WriteCommentOnCardResponse> writeCommentOnCard(@PathVariable("cardId") Long cardId) {
        final WriteCommentOnCardResponse writeCommentOnCardResponse = cardService.writeCommentOnCard(cardId);
        return ResponseEntity.ok(writeCommentOnCardResponse);
    }

}