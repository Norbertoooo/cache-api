package com.vitu.cache.api.repository;

import com.vitu.cache.api.domain.Hero;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeroRepository extends JpaRepository<Hero, Integer> {
}
