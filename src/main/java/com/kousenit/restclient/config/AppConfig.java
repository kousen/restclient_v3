package com.kousenit.restclient.config;

import com.kousenit.restclient.services.AstroInterface;
import com.kousenit.restclient.services.JsonPlaceholderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.Locale;

@Configuration
public class AppConfig {

    @Bean
    public NumberFormat defaultNumberFormat() {
        return NumberFormat.getCurrencyInstance(Locale.getDefault());
    }

    @Bean
    // @Scope("prototype")
    public NumberFormat indiaNumberFormat() {
        return NumberFormat.getCurrencyInstance(new Locale("hin", "IN"));
    }

    @Bean
    public AstroInterface astroInterface() {
        RestClient client = RestClient.create("http://api.open-notify.org");
        RestClientAdapter adapter = RestClientAdapter.create(client);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(AstroInterface.class);
    }

    @Bean
    public JsonPlaceholderService jsonPlaceholderService() {
        // Easy way (no error handling)
        // WebClient client = WebClient.create("https://jsonplaceholder.typicode.com");

        // Hard way (or at least harder way, with some error handling)
        WebClient client = WebClient.builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .defaultStatusHandler(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(
                                        new WebClientResponseException(
                                                response.statusCode().value(),
                                                "On the charge of Conduct Unbecoming, we find you guilty as charged",
                                                response.headers().asHttpHeaders(),
                                                body.getBytes(),
                                                Charset.defaultCharset()))))
                .build();

        WebClientAdapter adapter = WebClientAdapter.create(client);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(JsonPlaceholderService.class);
    }
}
