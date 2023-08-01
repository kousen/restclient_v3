package com.kousenit.restclient.services;

import com.kousenit.restclient.json.BlogPost;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.*;

import java.util.List;
import java.util.Optional;

@HttpExchange("/posts")
public interface JsonPlaceholderService {

    // Supposedly, returning ResponseEntity<Void> tells Spring
    // to do a HEAD request, but that doesn't seem to work.
    // Still seeing a GET request in the logs, along with headers
    // (but at least there's no body).
    @GetExchange()
    ResponseEntity<Void> headRequest();

    @GetExchange()
    List<BlogPost> getPosts();

    @GetExchange("{id}")
    Optional<BlogPost> getPost(@PathVariable int id);

    @PostExchange
    ResponseEntity<BlogPost> createPost(@RequestBody BlogPost post);

    @PutExchange("{id}")
    BlogPost updatePost(@PathVariable int id, @RequestBody BlogPost post);

    @DeleteExchange("{id}")
    void deletePost(@PathVariable int id);
}
