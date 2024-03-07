package com.kousenit.restclient.services;

import com.kousenit.restclient.json.AstroResponse;
import org.springframework.web.service.annotation.GetExchange;

public interface AstroInterface {
    @GetExchange("/astros.json")
    AstroResponse getAstroResponse();
}
