package com.kousenit.restclient.services;

import com.kousenit.restclient.json.BlogPost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@SpringBootTest
class JsonPlaceholderServiceTest {
    @Autowired
    private JsonPlaceholderService service;

    @BeforeEach
    void setUp() {
        WebTestClient client = WebTestClient.bindToServer()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .build();
        try {
            client.head()
                    .exchange()
                    .expectStatus().isOk();
        } catch (WebClientRequestException e) {
            assumeTrue(false, "JSON Placeholder not available");
        }
    }

    @Test
    void getAllPosts() {
        var posts = service.getPosts();
        assertEquals(100, posts.size());
        System.out.println(posts.get(0));
    }

    @Test
    void getPost_exists() {
        IntStream.rangeClosed(1, 100)
                .parallel()
                .forEach(i -> {
                    var post = service.getPost(i);
                    assertTrue(post.isPresent());
                    assertEquals(i, post.get().id());
                });
    }

    @Test
    void getPost_doesNotExist() {
        WebClientResponseException exception =
                assertThrows(WebClientResponseException.class,
                        () -> service.getPost(101));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        exception.getHeaders()
                .forEach((k, v) -> System.out.println(k + ": " + v));
        assertThat(exception.getStatusText()).contains("Conduct Unbecoming");
        System.out.println(exception.getStatusText());
    }

    @Test
    void createPost() {
        var post = new BlogPost(null, 1,
                "Test Post", "This is a test post.");
        var created = service.createPost(post);
        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        System.out.println(created);
        BlogPost blogPost = created.getBody();
        assertNotNull(blogPost);
        assertNotNull(blogPost.id());
    }

    @Test
    void updatePost() {
        var post = new BlogPost(1, 1,
                "Test Post", "This is a test post.");
        var updated = service.updatePost(1, post);
        System.out.println(updated);
        assertEquals(1, updated.id());
    }

    @Test
    void deletePost() {
        service.deletePost(1);
    }
}