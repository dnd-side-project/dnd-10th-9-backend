package com.dnd.dotchi.domain.card.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dnd.dotchi.domain.card.entity.Theme;

public interface ThemeRepository extends JpaRepository<Theme, Long> {
}
