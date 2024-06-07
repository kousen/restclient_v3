package com.kousenit.restclient.json;

public record BlogPost(
        Integer id,
        Integer userId,
        String title,
        String body) {
}
