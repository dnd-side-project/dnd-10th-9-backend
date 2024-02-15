package com.dnd.dotchi.domain.card.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dnd.dotchi.domain.card.dto.request.CardsByThemeRequest;
import com.dnd.dotchi.domain.card.dto.request.CardsWriteRequest;
import com.dnd.dotchi.domain.card.dto.response.CardsByThemeResponse;
import com.dnd.dotchi.domain.card.dto.response.CardsWriteResponse;
import com.dnd.dotchi.domain.card.exception.CardExceptionType;
import com.dnd.dotchi.domain.card.service.CardService;
import com.dnd.dotchi.global.exception.BadRequestException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController implements CardControllerDocs {

	final CardService cardService;

	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<CardsWriteResponse> write(
		@Valid @ModelAttribute
		final CardsWriteRequest request
	) {
		if (!request.image().getContentType().startsWith("image")) {
			throw new BadRequestException(CardExceptionType.NOT_IMAGE);
		}

		final CardsWriteResponse response = cardService.write(request);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/theme")
	public ResponseEntity<CardsByThemeResponse> getCardsByTheme(
		@Valid @ModelAttribute
		final CardsByThemeRequest request
	) {
		final CardsByThemeResponse response = cardService.getCardsByTheme(request);
		return ResponseEntity.ok(response);
	}

}