package com.vitu.cache.api.service;

import com.vitu.cache.api.domain.Hero;
import com.vitu.cache.api.repository.HeroRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HeroService {

    private final HeroRepository heroRepository;

    @Cacheable(value = "heroes")
    public List<Hero> findAll() {
        log.info("find all heroes");
        return heroRepository.findAll();
    }
}
