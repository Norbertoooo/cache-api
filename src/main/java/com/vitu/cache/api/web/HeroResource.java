package com.vitu.cache.api.web;

import com.vitu.cache.api.domain.Hero;
import com.vitu.cache.api.service.HeroService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/heroes")
public class HeroResource {

    private final HeroService heroService;

    @GetMapping
    public List<Hero> getAll() {
        log.info("Get all heroes");
        return heroService.findAll();
    }

    @GetMapping("/evict")
    @CacheEvict("heroes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void evictCache() {
        log.info("Evict cache");
    }

}
