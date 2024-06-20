package com.kousenit.restclient.services;

import com.kousenit.restclient.json.GeocoderResponse;
import com.kousenit.restclient.entities.Site;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GeocoderService {

    private final WebClient client;

    private static final String KEY;

    static {
        KEY = GeocoderService.getGoogleMapsApiKey();
    }

    public GeocoderService() {
        this.client = WebClient.create("https://maps.googleapis.com");
    }

    private static String getGoogleMapsApiKey() {
        return Optional.ofNullable(System.getenv("GOOGLE_MAPS_API_KEY"))
                .orElseThrow(() -> new RuntimeException("No API key found"));
    }

    private String encodeString(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    public Site getLatLng(String... address) {
        String encoded = Arrays.stream(address)
                .map(this::encodeString)
                .collect(Collectors.joining(","));
        String path = "/maps/api/geocode/json";
        GeocoderResponse geocoderResponse = client.get()
                .uri(uriBuilder -> uriBuilder.path(path)
                        .queryParam("address", encoded)
                        .queryParam("key", KEY)
                        .build()
                )
                .retrieve()
                .bodyToMono(GeocoderResponse.class)
                .log()
                .block(Duration.ofSeconds(2));
        assert geocoderResponse != null;
        return new Site(geocoderResponse.results().get(0).formattedAddress(),
                geocoderResponse.results().get(0).geometry().location().lat(),
                geocoderResponse.results().get(0).geometry().location().lng());
    }
}
