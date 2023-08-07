package com.kousenit.restclient.services;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class JokeServiceTest {
    private final Logger logger = LoggerFactory.getLogger(JokeService.class);

    @Autowired
    private JokeService service;

    @Test
    public void getJoke() {
        String joke = service.getJoke();
        logger.info(joke);
        assertTrue(joke.contains("Chuck") || joke.contains("Norris"));
    }

    @Test
    public void getJokeRestTemplate() {
        String joke = service.getJokeRT();
        logger.info(joke);
        assertTrue(joke.contains("Chuck") || joke.contains("Norris"));
    }
}