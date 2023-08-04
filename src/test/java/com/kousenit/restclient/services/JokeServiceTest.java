package com.kousenit.restclient.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@SpringBootTest
public class JokeServiceTest {
    private final Logger logger = LoggerFactory.getLogger(JokeService.class);

    @Autowired
    private JokeService service;

    @BeforeEach
    void setUp() {
        HttpResponse<Void> response = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.chucknorris.io"))
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.discarding());
        } catch (IOException | InterruptedException e) {
            assumeTrue(false, "Chuck Norris API is not available");
        }
        assumeTrue(response.statusCode() == HttpURLConnection.HTTP_OK);
    }

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