package com.dnd.dotchi.global.redis;

import org.springframework.data.repository.CrudRepository;

public interface MemberRedisRepository extends CrudRepository<CacheMember, Long> {
}
