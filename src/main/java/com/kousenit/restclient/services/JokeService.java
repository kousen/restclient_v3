package com.kousenit.restclient.services;

import com.kousenit.restclient.config.MyProperties;
import com.kousenit.restclient.json.JokeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class JokeService {
    private final RestClient restClient;

    @Autowired
    public JokeService(MyProperties properties) {
        restClient = RestClient.create(properties.getJokeUrl());
    }

    public JokeResponse getJoke() {
        return restClient.get()
                .uri("/jokes/random?category=dev")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(JokeResponse.class);
    }
}
