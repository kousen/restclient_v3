package com.kousenit.restclient.services;

import com.kousenit.restclient.entities.Site;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@SpringBootTest
class GeocoderServiceTest {
    private final Logger logger = LoggerFactory.getLogger(GeocoderServiceTest.class);
    private final static String BASE_URL = "https://maps.googleapis.com";

    @Autowired
    private GeocoderService service;

    @BeforeEach
    void setUp(@Autowired RestTemplateBuilder builder) {
        RestTemplate template = builder.build();
        try {
            HttpHeaders headers = template.headForHeaders(BASE_URL);
            System.out.println(headers);
            // For simplicity, just check the headers are not empty
            // Headers do NOT include a response status code, so consider
            // using a ResponseEntity instead
            assumeTrue(!headers.isEmpty(), "%s headers are not valid".formatted(BASE_URL));
        } catch (RestClientException e) {
            assumeTrue(false, "%s is not available".formatted(BASE_URL));
        }
    }

    @Test
    public void getLatLngWithoutStreet() {
        Site site = service.getLatLng("Boston", "MA");
        logger.info(site.toString());
        assertAll(
                () -> assertEquals(42.36, site.getLatitude(), 0.01),
                () -> assertEquals(-71.06, site.getLongitude(), 0.01),
                () -> assertNotNull(site.getAddress())
        );
    }

    @Test
    public void getLatLngWithStreet() {
        Site site = service.getLatLng("1600 Ampitheatre Parkway",
                "Mountain View", "CA");
        logger.info(site.toString());
        assertAll(
                () -> assertEquals(37.42, site.getLatitude(), 0.01),
                () -> assertEquals(-122.08, site.getLongitude(), 0.01)
        );
    }

    @Test
    void checkLocationsAroundTheWorld() {
        System.out.println(service.getLatLng("Moscow, Russia"));
        System.out.println(service.getLatLng("McMurdo Base, Antarctia"));
        System.out.println(service.getLatLng("Quito, Ecuador"));
    }
}