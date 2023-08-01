package com.oreilly.restclient.services;

import com.oreilly.restclient.json.BlogPost;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.stream.IntStream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JsonPlaceholderTest {
    @Autowired
    private WebTestClient client;

    @Test
    void getPosts() {
        client.get()
                .uri("https://jsonplaceholder.typicode.com/posts")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BlogPost.class)
                .hasSize(100);
    }

    @Test
    void getPost_exists() {
        IntStream.rangeClosed(1, 100)
                .parallel()
                .forEach(id -> client.get()
                        .uri("https://jsonplaceholder.typicode.com/posts/{id}", id)
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody(BlogPost.class)
                        .value(BlogPost::id, Matchers.equalTo(id)));
    }

    @Test
    void getPost_doesNotExist() {
        client.get()
                .uri("https://jsonplaceholder.typicode.com/posts/101")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void post() {
        var post = new BlogPost(null, 1,
                "Test Post", "This is a test post.");
        client.post()
                .uri("https://jsonplaceholder.typicode.com/posts")
                .bodyValue(post)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BlogPost.class)
                .value(BlogPost::id, Matchers.notNullValue())
                .consumeWith(System.out::println);
    }

    @Test
    void put() {
        var post = new BlogPost(1, 1,
                "Test Post", "This is a test post.");
        client.put()
                .uri("https://jsonplaceholder.typicode.com/posts/1")
                .bodyValue(post)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BlogPost.class)
                .value(BlogPost::id, Matchers.equalTo(1))
                .consumeWith(System.out::println);
    }

    @Test
    void delete() {
        client.delete()
                .uri("https://jsonplaceholder.typicode.com/posts/1")
                .exchange()
                //.expectStatus().isNoContent();
                .expectStatus().isOk();  // Service returns 200 when it should be 204
    }
}
