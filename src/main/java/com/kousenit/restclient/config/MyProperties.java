package com.kousenit.restclient.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("my.service")
public class MyProperties {
    private String jokeUrl;

    public String getJokeUrl() {
        return jokeUrl;
    }

    public void setJokeUrl(String jokeUrl) {
        this.jokeUrl = jokeUrl;
        System.out.println("baseurl: " + jokeUrl);
    }
}
