package com.kousenit.restclient.config;

import com.kousenit.restclient.services.AstroInterface;
import com.kousenit.restclient.services.JsonPlaceholderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

@Configuration
public class AppConfig {

    @Bean
    public AstroInterface astroInterface() {
        RestClient client = RestClient.create("http://api.open-notify.org");
        RestClientAdapter adapter = RestClientAdapter.create(client);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(AstroInterface.class);
    }

    @Bean
    public JsonPlaceholderService jsonPlaceholderService(
            @Value("${jsonplaceholder.baseurl}") String baseUrl) {
        // Easy way (no error handling)
        // WebClient client = WebClient.create(baseUrl);

        // Hard way (or at least harder way, with some error handling)
        WebClient client = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultStatusHandler(HttpStatusCode::isError, this::getThrowableMono)
                .build();
        WebClientAdapter adapter = WebClientAdapter.create(client);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(JsonPlaceholderService.class);
    }

    private Mono<Throwable> getThrowableMono(ClientResponse response) {
        return response.bodyToMono(String.class)
                .flatMap(body -> Mono.error(
                        new WebClientResponseException(
                                response.statusCode().value(),
                                "On the charge of Conduct Unbecoming, we find you guilty as charged",
                                response.headers().asHttpHeaders(),
                                body.getBytes(),
                                Charset.defaultCharset())));
    }
}
