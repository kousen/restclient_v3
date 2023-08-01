package com.oreilly.restclient.json;

public record BlogPost(Integer id,
                       Integer userId,
                       String title,
                       String body) {
}
