package com.vitu.cache.api.builder;

import com.vitu.cache.api.domain.Hero;

public class HeroBuilder {

    public static Hero buildHero() {
        return Hero.builder()
                .id(1)
                .name("all might")
                .age(35)
                .gender("masculino")
                .build();
    }
}
