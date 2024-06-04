package com.vitu.cache.api.integration;

import com.redis.testcontainers.RedisContainer;
import com.vitu.cache.api.repository.HeroRepository;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class IntegrationCacheTest {

    @Autowired
    MockMvc mockMvc;

    @SpyBean
    HeroRepository heroRepository;

    @Container
    private static final RedisContainer REDIS_CONTAINER = new RedisContainer(DockerImageName.parse("redis:6.2.6"))
            .withExposedPorts(6379);

    @DynamicPropertySource
    private static void registerRedisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379).toString());
    }

    @Test
    @Order(1)
    public void shouldNotUseCacheWhenGetHero() throws Exception {

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/heroes");

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(heroRepository, Mockito.times(1)).findAll();
    }

    @Test
    @Order(2)
    public void shouldUseCacheWhenGetHero() throws Exception {

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/heroes");

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(heroRepository, Mockito.never()).findAll();
    }

    @Test
    @Order(3)
    public void shouldNotUseCacheWhenGetHeroAfterCacheEvict() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/heroes/evict"))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/heroes"))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(heroRepository, Mockito.times(1)).findAll();
    }

}
