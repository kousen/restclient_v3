package com.kousenit.restclient.services;

import com.kousenit.restclient.json.BlogPost;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@ExtendWith(TotalTimeExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class JsonPlaceholderServicePerMethodTest {
    @Autowired
    private JsonPlaceholderService service;

    @BeforeAll
    static void isJsonPlaceholderAvailable() {
        HttpResponse<Void> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create("https://jsonplaceholder.typicode.com"))
                    .HEAD()
                    .build();
            response = client.send(req, HttpResponse.BodyHandlers.discarding());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assumeTrue(response != null && response.statusCode() == 200);
    }

    @Test
    void headRequest() {
        ResponseEntity<Void> entity = service.headRequest();
        assertAll(
                () -> assertNotNull(entity),
                () -> assertEquals(HttpStatus.OK, entity.getStatusCode()),
                () -> assertFalse(entity.hasBody())
        );
    }

    @Test
    void getAllPosts() {
        var posts = service.getPosts();
        assertEquals(100, posts.size());
    }

    @Execution(ExecutionMode.CONCURRENT)
    @ParameterizedTest(name = "Get post {0}")
    @MethodSource("getIndices")
    void getPost_exists(int id) {
        var post = service.getPost(id);
        assertTrue(post.isPresent());
        assertEquals(id, post.get().id());
    }

    static IntStream getIndices() {
        return IntStream.rangeClosed(1, 5);
    }

    @Test
    void getPost_doesNotExist() {
        WebClientResponseException exception =
                assertThrows(WebClientResponseException.class,
                        () -> service.getPost(101));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertThat(exception.getStatusText()).contains("Conduct Unbecoming");
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