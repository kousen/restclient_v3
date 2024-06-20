package com.kousenit.restclient.services.jokes;

import com.kousenit.restclient.json.JokeResponse;
import com.kousenit.restclient.services.JokeService;
import com.kousenit.restclient.services.TotalTimeExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
@ExtendWith(TotalTimeExtension.class)
public class JokeServiceTest {
    private final Logger logger = LoggerFactory.getLogger(JokeService.class);

    @Autowired
    private JokeService service;

    @BeforeEach
    void setUp() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.chucknorris.io"))
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .build();
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<Void> response = client.send(
                    request, HttpResponse.BodyHandlers.discarding());
            assumeTrue(response.statusCode() == HttpURLConnection.HTTP_OK);
        } catch (IOException | InterruptedException e) {
            assumeTrue(false, "Chuck Norris API is not available");
        }
    }

    @Test
    public void getJoke() {
        JokeResponse jokeResponse = service.getJoke();
        logger.info(jokeResponse.toString());
        String joke = jokeResponse.value();
        assertTrue(joke.contains("Chuck") || joke.contains("Norris"));
    }

}