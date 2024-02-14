package com.dnd.dotchi.domain.card.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dnd.dotchi.domain.card.dto.request.CardsByThemeRequest;
import com.dnd.dotchi.domain.card.dto.request.CardsWriteRequest;
import com.dnd.dotchi.domain.card.dto.response.CardsByThemeResponse;
import com.dnd.dotchi.domain.card.dto.response.CardsWriteResponse;
import com.dnd.dotchi.domain.card.service.CardService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController implements CardControllerDocs {

	final CardService cardService;

	@PostMapping
	public ResponseEntity write(
		@Valid
		@RequestBody CardsWriteRequest request
	) {
		final CardsWriteResponse response = cardService.write(request);
		return ResponseEntity.ok(response);
	}

    @GetMapping("/theme")
    public ResponseEntity<CardsByThemeResponse> getCardsByTheme(
            @Valid @ModelAttribute CardsByThemeRequest request
    ) {
        final CardsByThemeResponse response = cardService.getCardsByTheme(request);
        return ResponseEntity.ok(response);
    }

}