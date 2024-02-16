package com.dnd.dotchi.domain.blacklist.repository;

import com.dnd.dotchi.domain.blacklist.entity.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackListRepository extends JpaRepository<BlackList, Long> {
}
