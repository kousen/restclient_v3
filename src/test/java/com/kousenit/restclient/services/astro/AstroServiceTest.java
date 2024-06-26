package com.kousenit.restclient.services.astro;

import com.kousenit.restclient.json.AstroResponse;
import com.kousenit.restclient.services.AstroService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@SpringBootTest
@ExtendWith(TotalTimeExtension.class)
public class AstroServiceTest {
    private final Logger logger = LoggerFactory.getLogger(AstroService.class);

    @Autowired
    private AstroService service;

    @BeforeEach
    void setUp() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://api.open-notify.org"))
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .build();
        try(HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<Void> response =
                    client.send(request, HttpResponse.BodyHandlers.discarding());
            assumeTrue(response.statusCode() == HttpURLConnection.HTTP_OK);
        } catch (IOException | InterruptedException e) {
            assumeTrue(false, "Astro Service is not available");
        }
    }

    @Test
    public void getAstroResponse() {
        AstroResponse response = service.getAstroResponse();
        logger.info(response.toString());
        assertAll(
                () -> assertTrue(response.number() >= 0),
                () -> assertEquals("success", response.message()),
                () -> assertEquals(response.number(), response.people().size())
        );
    }

    @Test
    public void getAstroResponseRT() {
        AstroResponse response = service.getAstroResponseRT();
        logger.info(response.toString());
        assertAll(
                () -> assertTrue(response.number() >= 0),
                () -> assertEquals("success", response.message()),
                () -> assertEquals(response.number(), response.people().size())
        );
    }

    @Test
    void getAstroResponseRC() {
        AstroResponse response = service.getAstroResponseRC();
        logger.info(response.toString());
        assertAll(
                () -> assertTrue(response.number() >= 0),
                () -> assertEquals("success", response.message()),
                () -> assertEquals(response.number(), response.people().size())
        );
    }
}